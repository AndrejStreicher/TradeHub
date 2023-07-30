package com.example.demo;

import com.example.demo.model.ClusterInsiderBuysModel;
import com.example.demo.service.TelegramMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class TelegramMessageTests
{
    private final TelegramMessageService telegramMessageService;

    @Autowired
    public TelegramMessageTests(TelegramMessageService telegramMessageService)
    {
        this.telegramMessageService = telegramMessageService;
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
        HttpResponse<String> response = telegramMessageService.sendMessage(TelegramMessageService.TargetChat.TEST, clusterInsiderBuysModelTest);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.toString().contains("200"));
    }
}
