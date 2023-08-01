package com.trading.tradehub;

import com.trading.tradehub.service.YahooFinanceWebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YahooFinanceWebScraperTests
{
    private final YahooFinanceWebScraperService yahooFinanceWebScraperService;

    @Autowired
    public YahooFinanceWebScraperTests(YahooFinanceWebScraperService yahooFinanceWebScraperService)
    {
        this.yahooFinanceWebScraperService = yahooFinanceWebScraperService;
    }
}
