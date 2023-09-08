package com.trading.tradehub.service.telegram;

import com.trading.tradehub.model.ClusterInsiderBuyModel;
import com.trading.tradehub.model.TickerSummaryModel;
import com.trading.tradehub.util.UtilHTMLMethods;
import com.trading.tradehub.util.UtilStringMethods;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

/**
 * Service class to send messages to Telegram chat groups or users using Telegram Bot API.
 */
@Service
public class TelegramBotService
{
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
    private final HttpClient httpClient = HttpClient.newHttpClient();
    @Value("${telegram.chatID.personal}")
    private String personalChatID;
    @Value("${telegram.chatID.test}")
    private String testGroupChatID;
    @Value("${telegram.chatID.group}")
    private String groupChatID;
    @Value("${telegram.botAPI.key}")
    private String telegramBotAPIKey;
    @Value("${server.domain}")
    private String serverDomain;

    /**
     * Enum representing the target chat group or user for sending the message.
     */
    public enum TargetChat
    {
        PERSONAL,
        GROUP,
        TEST
    }

    /**
     * Send a message to the specified Telegram chat group or user.
     *
     * @param targetChat             The target chat group or user to send the message to.
     * @param clusterInsiderBuyModel The ClusterInsiderBuysModel object containing information to include in the message.
     */
    public void sendMessage(TargetChat targetChat, ClusterInsiderBuyModel clusterInsiderBuyModel)
    {
        sendMessageToTelegram(targetChat, buildClusterBuyMessage(clusterInsiderBuyModel));
    }

    public void sendMessage(TargetChat targetChat, TickerSummaryModel tickerSummaryModel)
    {
        sendMessageToTelegram(targetChat, buildTickerSummaryMessage(tickerSummaryModel));
    }

    public void sendMessage(TargetChat targetChat, String message)
    {
        sendMessageToTelegram(targetChat, message);
    }


    private void sendMessageToTelegram(TargetChat targetChat, String message)
    {
        HttpRequest request = buildMessageRequest(targetChat, message);
        try
        {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e)
        {
            logger.error("Error occurred while processing the data.", e);
        } catch (InterruptedException e)
        {
            logger.error("Thread was interrupted during data processing.", e);
            Thread.currentThread().interrupt();
        }
    }

    @PostConstruct
    private void setTelegramUpdateWebhook()
    {
        String urlString = getBaseBotUrl() + "/setWebhook" + "?url=" + serverDomain + "telegram/webhook";
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(urlString)).GET().build();
        try
        {
            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e)
        {
            logger.error("Error occurred while processing the data.", e);
        } catch (InterruptedException e)
        {
            logger.error("Thread was interrupted during data processing.", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Build the HttpRequest to send the message to the Telegram Bot API.
     *
     * @param target        The target chat group or user to send the message to.
     * @param messageToSend The message to be sent.
     * @return The HttpRequest object.
     */
    private HttpRequest buildMessageRequest(TargetChat target, String messageToSend)
    {
        String targetChat = switch (target)
        {
            case PERSONAL -> personalChatID;
            case GROUP -> groupChatID;
            case TEST -> testGroupChatID;
        };
        String urlString = getBaseBotUrl() + "/sendMessage" + "?chat_id=" + targetChat + "&text=" + messageToSend + "&parse_mode=HTML";
        return HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();
    }

    /**
     * Build the message to be sent to the Telegram chat.
     *
     * @param clusterInsiderBuyModel The ClusterInsiderBuysModel object containing information to include in the message.
     * @return The formatted message to be sent.
     */
    private String buildClusterBuyMessage(ClusterInsiderBuyModel clusterInsiderBuyModel)
    {
        String messageToSend = UtilHTMLMethods.HTMLFileToString("src/main/resources/static/clusterBuyTelegramMessage.html");
        assert messageToSend != null;
        messageToSend = String.format(messageToSend,
                clusterInsiderBuyModel.ticker(),
                clusterInsiderBuyModel.ticker(),
                clusterInsiderBuyModel.companyName(),
                clusterInsiderBuyModel.industry(),
                clusterInsiderBuyModel.amountOfInsiders(),
                clusterInsiderBuyModel.price(),
                clusterInsiderBuyModel.quantity(),
                clusterInsiderBuyModel.ownedShares(),
                clusterInsiderBuyModel.changeInOwnedShares(),
                clusterInsiderBuyModel.value(),
                clusterInsiderBuyModel.filingDate(),
                clusterInsiderBuyModel.tradeDate());
        return URLEncoder.encode(messageToSend, StandardCharsets.UTF_8);
    }

    private String buildTickerSummaryMessage(TickerSummaryModel tickerSummaryModel)
    {
        String messageToSend = UtilHTMLMethods.HTMLFileToString("src/main/resources/static/tickerInfoTelegramMessage.html");
        assert messageToSend != null;
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        String marketOpenMessage;
        if (tickerSummaryModel.isOpen())
        {
            marketOpenMessage = "Open";
        } else
        {
            marketOpenMessage = "Closed";
        }
        String volumeString = decimalFormat.format(tickerSummaryModel.volume());
        double currentPrice = tickerSummaryModel.currentPrice().isEmpty() ? 0.0 : tickerSummaryModel.currentPrice().get();
        String averageVolumeString = decimalFormat.format(tickerSummaryModel.averageVolume());
        messageToSend = String.format(messageToSend,
                tickerSummaryModel.ticker(),
                tickerSummaryModel.companyName(),
                tickerSummaryModel.marketCap(),
                marketOpenMessage,
                UtilStringMethods.parseDoubleToString(currentPrice),
                UtilStringMethods.parseDoubleToString(tickerSummaryModel.changeSinceOpen()),
                UtilStringMethods.parseDoubleToString(tickerSummaryModel.changeSinceOpenPercent()),
                UtilStringMethods.parseDoubleToString(tickerSummaryModel.fiftyTwoWeekLow()),
                UtilStringMethods.parseDoubleToString(tickerSummaryModel.fiftyTwoWeekHigh()),
                volumeString,
                averageVolumeString
        );
        return URLEncoder.encode(messageToSend, StandardCharsets.UTF_8);
    }

    private String getBaseBotUrl()
    {
        return "https://api.telegram.org/bot" + telegramBotAPIKey;
    }
}
