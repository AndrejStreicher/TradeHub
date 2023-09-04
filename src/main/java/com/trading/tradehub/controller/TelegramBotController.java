package com.trading.tradehub.controller;

import com.trading.tradehub.model.TelegramUpdateModel;
import com.trading.tradehub.service.telegram.TelegramCommandService;
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
    private final TelegramCommandService telegramCommandService;

    @Autowired
    public TelegramBotController(TelegramCommandService telegramCommandService)
    {
        this.telegramCommandService = telegramCommandService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody TelegramUpdateModel update)
    {
        telegramCommandService.handleUpdate(update);
        return ResponseEntity.ok("OK");
    }
}
