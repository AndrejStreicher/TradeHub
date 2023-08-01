package com.trading.tradehub;

import com.trading.tradehub.service.YahooFinanceWebScraperService;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

    @Test
    void getQuoteTest()
    {
        Document quoteDocument = yahooFinanceWebScraperService.getQuote("AAPL");
        Assertions.assertNotNull(quoteDocument);
    }
}
