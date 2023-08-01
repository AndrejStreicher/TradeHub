package com.trading.tradehub;

import com.trading.tradehub.model.ClusterInsiderBuysModel;
import com.trading.tradehub.service.TelegramBotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class TelegramBotServiceTests
{
    private final TelegramBotService telegramBotService;
    private final ClusterInsiderBuysModel clusterInsiderBuysModelTest;

    @Autowired
    public TelegramBotServiceTests(TelegramBotService telegramBotService)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        clusterInsiderBuysModelTest = new ClusterInsiderBuysModel(
                "2M",
                LocalDateTime.parse("2023-07-26 12:04:47", dateTimeFormatter),
                LocalDate.parse("2023-07-25", dateFormatter),
                "TEST",
                "Test Company",
                "Testing Industry",
                4,
                "P - Purchase",
                4.50,
                100,
                150,
                16,
                4560320);
        this.telegramBotService = telegramBotService;
    }

    @Test
    void groupMessageClusterBuyTest()
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ClusterInsiderBuysModel clusterInsiderBuysModelTest = new ClusterInsiderBuysModel(
                "2M",
                LocalDateTime.parse("2023-07-26 12:04:47", dateTimeFormatter),
                LocalDate.parse("2023-07-25", dateFormatter),
                "TEST",
                "Test Company",
                "Testing Industry",
                4,
                "P - Purchase",
                4.50,
                100,
                150,
                16,
                4560320);
        HttpResponse<String> response = telegramBotService.sendMessage(TelegramBotService.TargetChat.TEST, clusterInsiderBuysModelTest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void getUpdatesTest()
    {
        System.out.println(telegramBotService.getBotUpdates() + "___________");
    }
}
