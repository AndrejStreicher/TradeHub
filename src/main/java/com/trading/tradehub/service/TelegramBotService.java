package com.trading.tradehub.service;

import com.trading.tradehub.model.ClusterInsiderBuysModel;
import com.trading.tradehub.util.UtilClasses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    /**
     * Enum representing the target chat group or user for sending the message.
     */
    public enum TargetChat
    {
        PERSONAL,
        GROUP,
        TEST
    }

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String telegramBotAPIURL = "https://api.telegram.org/bot";
    @Value("${telegram.chatID.personal}")
    private String personalChatID;
    @Value("${telegram.chatID.test}")
    private String testGroupChatID;
    @Value("${telegram.chatID.group}")
    private String groupChatID;
    @Value("${telegram.botAPI.key}")
    private String telegramBotAPIKey;

    /**
     * Send a message to the specified Telegram chat group or user.
     *
     * @param targetChat              The target chat group or user to send the message to.
     * @param clusterInsiderBuysModel The ClusterInsiderBuysModel object containing information to include in the message.
     * @return An HttpResponse containing the response from the Telegram Bot API.
     */
    public HttpResponse<String> sendMessage(TargetChat targetChat, ClusterInsiderBuysModel clusterInsiderBuysModel)
    {
        HttpRequest request = buildMessageRequest(targetChat, buildClusterBuyMessage(clusterInsiderBuysModel));
        try
        {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
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
     * @param clusterInsiderBuysModel The ClusterInsiderBuysModel object containing information to include in the message.
     * @return The formatted message to be sent.
     */
    private String buildClusterBuyMessage(ClusterInsiderBuysModel clusterInsiderBuysModel)
    {
        String messageToSend = UtilClasses.HTMLFileToString("src/main/resources/static/clusterBuyTelegramMessage.html");
        assert messageToSend != null;
        messageToSend = String.format(messageToSend,
                clusterInsiderBuysModel.ticker(),
                clusterInsiderBuysModel.ticker(),
                clusterInsiderBuysModel.companyName(),
                clusterInsiderBuysModel.industry(),
                clusterInsiderBuysModel.amountOfInsiders(),
                clusterInsiderBuysModel.price(),
                clusterInsiderBuysModel.quantity(),
                clusterInsiderBuysModel.ownedShares(),
                clusterInsiderBuysModel.changeInOwnedShares(),
                clusterInsiderBuysModel.value(),
                clusterInsiderBuysModel.filingDate(),
                clusterInsiderBuysModel.tradeDate());
        messageToSend = URLEncoder.encode(messageToSend, StandardCharsets.UTF_8);
        return messageToSend;
    }

    private String getBaseBotUrl()
    {
        return telegramBotAPIURL + telegramBotAPIKey;
    }

    public String getBotUpdates()
    {
        String urlString = getBaseBotUrl() + "/getUpdates";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(urlString, String.class);
    }
}
