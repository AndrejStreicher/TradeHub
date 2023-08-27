package com.trading.tradehub.service.telegram;

import com.trading.tradehub.model.TelegramUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramCommandService
{
    private TelegramBotService telegramBotService;

    @Autowired
    public TelegramCommandService(TelegramBotService telegramBotService)
    {
        this.telegramBotService = telegramBotService;
    }

    public void handleUpdate(TelegramUpdateModel update)
    {
        if (update.message().text().contains("/ticker"))
        {
            String ticker = update.message().text().replace("/ticker", "");
            CommandIsTicker(ticker);
        }
    }

    private void CommandIsTicker(String ticker)
    {
        telegramBotService.sendMessage(TelegramBotService.TargetChat.PERSONAL, "Ticker " + ticker);
    }
}
