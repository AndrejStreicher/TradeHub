package com.trading.tradehub.controller;

import com.trading.tradehub.model.TelegramUpdateModel;
import com.trading.tradehub.service.telegram.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram")
public class TelegramBotController
{
    private final TelegramBotService telegramBotService;

    @Autowired
    public TelegramBotController(TelegramBotService telegramBotService)
    {
        this.telegramBotService = telegramBotService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody TelegramUpdateModel update)
    {
        return ResponseEntity.ok("OK");
    }
}
