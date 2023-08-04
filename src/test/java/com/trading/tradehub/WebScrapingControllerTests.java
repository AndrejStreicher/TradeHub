package com.trading.tradehub;

import com.trading.tradehub.controller.WebScrapingController;
import com.trading.tradehub.model.ClusterInsiderBuysModel;
import com.trading.tradehub.model.TickerInsiderTradeModel;
import com.trading.tradehub.service.OpenInsiderWebScraperService;
import com.trading.tradehub.service.YahooFinanceWebScraperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.when;

@WebMvcTest(WebScrapingController.class)
class WebScrapingControllerTests
{
    @MockBean
    private OpenInsiderWebScraperService openInsiderWebScraperService;
    @MockBean
    private YahooFinanceWebScraperService yahooFinanceWebScraperService;

    @Autowired
    private WebScrapingController webScrapingController;

    @Test
    void getLatestClusterBuys_ReturnsNonEmptyList_StatusCode200()
    {
        // Create mock data
        List<ClusterInsiderBuysModel> clusterInsiderBuysModels = new ArrayList<>();
        clusterInsiderBuysModels.add(new ClusterInsiderBuysModel(
                "M",
                LocalDateTime.now(),
                LocalDate.of(2023, 8, 1),
                "AAPL",
                "Apple Inc.",
                "Technology",
                5,
                "Buy",
                145.67,
                1000,
                2000,
                100,
                145670
        ));

        when(openInsiderWebScraperService.scrapeLatestClusterBuys()).thenReturn(clusterInsiderBuysModels);

        ResponseEntity<List<ClusterInsiderBuysModel>> response = webScrapingController.getLatestClusterBuys();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(clusterInsiderBuysModels, response.getBody());
    }

    @Test
    void getLatestClusterBuys_ReturnsEmptyList_StatusCode400()
    {
        List<ClusterInsiderBuysModel> clusterInsiderBuysModels = Collections.emptyList();
        when(openInsiderWebScraperService.scrapeLatestClusterBuys()).thenReturn(clusterInsiderBuysModels);
        ResponseEntity<List<ClusterInsiderBuysModel>> response = webScrapingController.getLatestClusterBuys();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getTickerInsiderTrades_ReturnsNonEmptyList_StatusCode200()
    {
        List<TickerInsiderTradeModel> tickerInsiderTradeModels = new ArrayList<>();
        tickerInsiderTradeModels.add(new TickerInsiderTradeModel(
                "X",
                LocalDateTime.now(),
                LocalDate.of(2023, 8, 1),
                "TEST",
                "Chad Broski",
                "CEO",
                "P - Purchase",
                6.52,
                350,
                23,
                4,
                23654));
        when(openInsiderWebScraperService.scrapeLatestTickerClusterBuys("TEST")).thenReturn(tickerInsiderTradeModels);

        ResponseEntity<List<TickerInsiderTradeModel>> response = webScrapingController.getTickerInsiderTrades("TEST");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(tickerInsiderTradeModels, response.getBody());
    }

    @Test
    void getTickerInsiderTrades_ReturnsCollectionsDotEmptyList_StatusCode400()
    {
        List<TickerInsiderTradeModel> tickerInsiderTradeModels = Collections.emptyList();
        when(openInsiderWebScraperService.scrapeLatestTickerClusterBuys("TEST")).thenReturn(tickerInsiderTradeModels);

        ResponseEntity<List<TickerInsiderTradeModel>> response = webScrapingController.getTickerInsiderTrades("TEST");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getTickerInsiderTrades_TickerIsNull_StatusCode400()
    {
        ResponseEntity<List<TickerInsiderTradeModel>> response = webScrapingController.getTickerInsiderTrades(null);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getTickerInsiderTrades_ReturnsEmptyArrayList_StatusIs204()
    {
        List<TickerInsiderTradeModel> tickerInsiderTradeModels = new ArrayList<>();
        when(openInsiderWebScraperService.scrapeLatestTickerClusterBuys("TEST")).thenReturn(tickerInsiderTradeModels);

        ResponseEntity<List<TickerInsiderTradeModel>> response = webScrapingController.getTickerInsiderTrades("TEST");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getCurrentPrice_ReturnsNonNullOptionalDouble_StatusCode200()
    {
        Optional<Double> currentPrice = Optional.of(65.22);
        when(yahooFinanceWebScraperService.getCurrentPrice("TEST")).thenReturn(currentPrice);

        ResponseEntity<Optional<Double>> response = webScrapingController.getCurrentPrice("TEST");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(currentPrice, response.getBody());
    }

    @Test
    void getCurrentPrice_ReturnsNullOptionalDouble_StatusCode400()
    {
        Optional<Double> currentPrice = Optional.empty();
        when(yahooFinanceWebScraperService.getCurrentPrice("TEST")).thenReturn(currentPrice);
        ResponseEntity<Optional<Double>> response = webScrapingController.getCurrentPrice("TEST");

        Assertions.assertThrowsExactly(NoSuchElementException.class, currentPrice::get);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getCurrentPrice_TickerIsNull_StatusCode400()
    {
        ResponseEntity<Optional<Double>> response = webScrapingController.getCurrentPrice(null);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}
