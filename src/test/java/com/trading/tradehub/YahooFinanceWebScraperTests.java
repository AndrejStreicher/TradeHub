package com.trading.tradehub;

import com.trading.tradehub.model.HistoricalDataModel;
import com.trading.tradehub.service.YahooFinanceWebScraperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

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
    void getHistoricalDataTestWithGoodInfo()
    {
        List<HistoricalDataModel> historicalDataModels = yahooFinanceWebScraperService.getHistoricalData("AAPL", LocalDate.of(2022, 8, 11), LocalDate.of(2022, 8, 18), "1d");
        Assertions.assertNotNull(historicalDataModels);
        Assertions.assertFalse(historicalDataModels.isEmpty());
    }
}
