package com.trading.tradehub;

import com.trading.tradehub.controller.WebScrapingController;
import com.trading.tradehub.model.ClusterInsiderBuyModel;
import com.trading.tradehub.model.FundamentalTickerDataModel;
import com.trading.tradehub.model.TickerInsiderTradeModel;
import com.trading.tradehub.service.FinvizWebScraperService;
import com.trading.tradehub.service.OpenInsiderWebScraperService;
import com.trading.tradehub.service.YahooFinanceWebScraperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebMvcTest(WebScrapingController.class)
class WebScrapingControllerTests
{
    @MockBean
    private OpenInsiderWebScraperService openInsiderWebScraperService;
    @MockBean
    private YahooFinanceWebScraperService yahooFinanceWebScraperService;
    @MockBean
    private FinvizWebScraperService finvizWebScraperService;

    private WebScrapingController webScrapingController;

    @BeforeEach
    void setUp()
    {
        webScrapingController = new WebScrapingController(openInsiderWebScraperService, yahooFinanceWebScraperService, finvizWebScraperService);
    }

    @Test
    void getLatestClusterBuys_GetsNonEmptyList_StatusCode200()
    {
        // Create mock data
        List<ClusterInsiderBuyModel> clusterInsiderBuyModels = new ArrayList<>();
        clusterInsiderBuyModels.add(mock(ClusterInsiderBuyModel.class));

        when(openInsiderWebScraperService.scrapeLatestClusterBuys()).thenReturn(clusterInsiderBuyModels);

        ResponseEntity<List<ClusterInsiderBuyModel>> response = webScrapingController.getLatestClusterBuys();
        
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(clusterInsiderBuyModels, response.getBody());
    }

    @Test
    void getLatestClusterBuys_GetsEmptyList_StatusCode400()
    {
        List<ClusterInsiderBuyModel> clusterInsiderBuyModels = Collections.emptyList();
        when(openInsiderWebScraperService.scrapeLatestClusterBuys()).thenReturn(clusterInsiderBuyModels);

        ResponseEntity<List<ClusterInsiderBuyModel>> response = webScrapingController.getLatestClusterBuys();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getTickerInsiderTrades_GetsNonEmptyList_StatusCode200()
    {
        List<TickerInsiderTradeModel> tickerInsiderTradeModels = new ArrayList<>();
        tickerInsiderTradeModels.add(mock(TickerInsiderTradeModel.class));
        when(openInsiderWebScraperService.scrapeLatestTickerClusterBuys("TEST")).thenReturn(tickerInsiderTradeModels);

        ResponseEntity<List<TickerInsiderTradeModel>> response = webScrapingController.getTickerInsiderTrades("TEST");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(tickerInsiderTradeModels, response.getBody());
    }

    @Test
    void getTickerInsiderTrades_GetsCollectionDotEmpty_StatusCode400()
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
    void getTickerInsiderTrades_GetsEmptyArrayList_StatusIs204()
    {
        List<TickerInsiderTradeModel> tickerInsiderTradeModels = new ArrayList<>();
        when(openInsiderWebScraperService.scrapeLatestTickerClusterBuys("TEST")).thenReturn(tickerInsiderTradeModels);

        ResponseEntity<List<TickerInsiderTradeModel>> response = webScrapingController.getTickerInsiderTrades("TEST");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getCurrentPrice_GetsNonNullOptionalDouble_StatusCode200()
    {
        Optional<Double> currentPrice = Optional.of(65.22);
        when(yahooFinanceWebScraperService.getCurrentPrice("TEST")).thenReturn(currentPrice);

        ResponseEntity<Optional<Double>> response = webScrapingController.getCurrentPrice("TEST");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(currentPrice, response.getBody());
    }

    @Test
    void getCurrentPrice_GetsNullOptionalDouble_StatusCode400()
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

    @Test
    void getFundamentalData_TickerIsNull_StatusCode400()
    {
        ResponseEntity<FundamentalTickerDataModel> response = webScrapingController.getFundamentalData(null);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getFundamentalData_GetsValidFundamentalDataModelObject_StatusCode200()
    {
        String ticker = "aapl";
        FundamentalTickerDataModel fundamentalTickerDataModelMocked = Mockito.mock(FundamentalTickerDataModel.class);
        when(finvizWebScraperService.getFundamentalInfo(Mockito.anyString())).thenReturn(fundamentalTickerDataModelMocked);

        ResponseEntity<FundamentalTickerDataModel> response = webScrapingController.getFundamentalData(ticker);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(fundamentalTickerDataModelMocked, response.getBody());
    }

    @Test
    void getScreenedClusterBuys_GetsValidLink_StatusCode200()
    {
        String link = "http://openinsider.com/screener?s=&o=&pl=3&ph=&ll=&lh=&fd=90&fdr=&td=0&tdr=&fdlyl=&fdlyh=6&daysago=&xp=1&vl=25&vh=&ocl=1&och=&sic1=-1&sicl=100&sich=9999&grp=2&nfl=&nfh=&nil=6&nih=&nol=1&noh=&v2l=&v2h=&oc2l=&oc2h=&sortcol=0&cnt=200&page=1";
        List<ClusterInsiderBuyModel> clusterInsiderBuyModelList = new ArrayList<>();
        ClusterInsiderBuyModel clusterInsiderBuyModel = Mockito.mock(ClusterInsiderBuyModel.class);
        clusterInsiderBuyModelList.add(clusterInsiderBuyModel);

        when(openInsiderWebScraperService.scrapeScreenedClusterBuys(link)).thenReturn(clusterInsiderBuyModelList);
        ResponseEntity<List<ClusterInsiderBuyModel>> response = webScrapingController.getScreenedClusterBuys(link);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getScreenedClusterBuys_GetsInvalidLink_StatusCode400()
    {
        String link = "http://openinsom/screener?s=&o=&pdr=&fdlyl=&fdlyh=6&daysago=&xp=1&vl=25&vh=&ocl=1&och=&sic1=-1&sicl=100&sich=9999&grp=2&nfl=&nfh=&nil=6&nih=&nol=1&noh=&v2l=&v2h=&oc2l=&oc2h=&sortcol=0&cnt=200&page=1";

        ResponseEntity<List<ClusterInsiderBuyModel>> response = webScrapingController.getScreenedClusterBuys(link);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}
