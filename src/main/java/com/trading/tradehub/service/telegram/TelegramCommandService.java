package com.trading.tradehub.service.telegram;

import com.trading.tradehub.model.TelegramUpdateModel;
import com.trading.tradehub.model.TickerSummaryModel;
import com.trading.tradehub.service.webscraping.YahooFinanceWebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramCommandService
{
    private final TelegramBotService telegramBotService;
    private final YahooFinanceWebScraperService yahooFinanceWebScraperService;
    private static final String TICKER_COMMAND = "/ticker";

    @Autowired
    public TelegramCommandService(TelegramBotService telegramBotService, YahooFinanceWebScraperService yahooFinanceWebScraperService)
    {
        this.telegramBotService = telegramBotService;
        this.yahooFinanceWebScraperService = yahooFinanceWebScraperService;
    }

    public void handleUpdate(TelegramUpdateModel update)
    {
        if (update.message().text().contains(TICKER_COMMAND))
        {
            String ticker = update.message().text().replace(TICKER_COMMAND, "");
            commandIsTicker(ticker);
        }
    }

    private void commandIsTicker(String ticker)
    {
        TickerSummaryModel tickerSummaryModel = yahooFinanceWebScraperService.getTickerSummary(ticker);
        telegramBotService.sendMessage(TelegramBotService.TargetChat.PERSONAL, tickerSummaryModel);
    }
}
