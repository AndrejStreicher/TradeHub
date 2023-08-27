package com.trading.tradehub.service.messaging;

import com.trading.tradehub.model.ClusterInsiderBuyModel;
import com.trading.tradehub.util.UtilHTMLMethods;
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
        HttpRequest request = buildMessageRequest(targetChat, buildClusterBuyMessage(clusterInsiderBuyModel));
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
        messageToSend = URLEncoder.encode(messageToSend, StandardCharsets.UTF_8);
        return messageToSend;
    }

    private String getBaseBotUrl()
    {
        return "https://api.telegram.org/bot" + telegramBotAPIKey;
    }
}
