package com.trading.tradehub.controller;

import com.trading.tradehub.model.ClusterInsiderBuyModel;
import com.trading.tradehub.model.FundamentalTickerDataModel;
import com.trading.tradehub.model.HistoricalDataModel;
import com.trading.tradehub.model.TickerInsiderTradeModel;
import com.trading.tradehub.service.webscraping.FinvizWebScraperService;
import com.trading.tradehub.service.webscraping.OpenInsiderWebScraperService;
import com.trading.tradehub.service.webscraping.YahooFinanceWebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/webscraping")
public class WebScrapingController
{

    private final OpenInsiderWebScraperService openInsiderWebScraperService;
    private final YahooFinanceWebScraperService yahooFinanceWebScraperService;
    private final FinvizWebScraperService finvizWebScraperService;

    @Autowired
    public WebScrapingController(OpenInsiderWebScraperService openInsiderWebScraperService, YahooFinanceWebScraperService yahooFinanceWebScraperService, FinvizWebScraperService finvizWebScraperService)
    {
        this.openInsiderWebScraperService = openInsiderWebScraperService;
        this.yahooFinanceWebScraperService = yahooFinanceWebScraperService;
        this.finvizWebScraperService = finvizWebScraperService;
    }

    /**
     * Endpoint to retrieve the latest cluster insider buys.
     *
     * @return ResponseEntity containing a list of ClusterInsiderBuysModel if available, else a 404 Not Found response.
     */
    @GetMapping("openinsider/clusterbuys")
    public ResponseEntity<List<ClusterInsiderBuyModel>> getLatestClusterBuys()
    {
        List<ClusterInsiderBuyModel> clusterBuys = openInsiderWebScraperService.scrapeLatestClusterBuys();
        if (clusterBuys.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clusterBuys);
    }

    @GetMapping("openinsider/clusterbuys/screener/{link}")
    public ResponseEntity<List<ClusterInsiderBuyModel>> getScreenedClusterBuys(@PathVariable String link)
    {
        List<ClusterInsiderBuyModel> clusterBuys = openInsiderWebScraperService.scrapeScreenedClusterBuys(link);
        if (clusterBuys.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clusterBuys);
    }

    /**
     * Endpoint to retrieve the latest insider buys for a specific ticker.
     *
     * @param ticker The ticker symbol for the stock.
     * @return ResponseEntity containing a list of TickerInsiderBuysModel if available.
     * If the ticker is empty or null, a 400 Bad Request response is returned.
     */
    @GetMapping("openinsider/{ticker}")
    public ResponseEntity<List<TickerInsiderTradeModel>> getTickerInsiderTrades(@PathVariable String ticker)
    {
        // Input validation: Check if the ticker is not empty or null
        if (ticker == null || ticker.trim().isEmpty())
        {
            // If the ticker is invalid, return a 400 Bad Request response to the client
            return ResponseEntity.badRequest().build();
        }

        List<TickerInsiderTradeModel> insiderTrades = openInsiderWebScraperService.scrapeLatestTickerClusterBuys(ticker);
        if (!(insiderTrades instanceof ArrayList<TickerInsiderTradeModel>))
        {
            // If the ticker is invalid return 400 Bad Request response.
            return ResponseEntity.badRequest().build();
        }
        if (insiderTrades.isEmpty())
        {
            // If the ticker is valid, but cluster buy data is found return 204 No content response.
            return ResponseEntity.noContent().build();
        }
        // Return the response with the list of TickerInsiderBuysModel
        return ResponseEntity.ok(insiderTrades);
    }

    /**
     * Endpoint to retrieve the latest insider buys for a specific ticker.
     *
     * @param ticker The ticker symbol for the stock.
     * @return ResponseEntity containing the current price of the stock if available.
     * If the ticker is empty or null, a 400 Bad Request response is returned.
     */
    @GetMapping("yahoofinance/currentprice/{ticker}")
    public ResponseEntity<Optional<Double>> getCurrentPrice(@PathVariable String ticker)
    {
        // Input validation: Check if the ticker is not empty or null
        if (ticker == null || ticker.trim().isEmpty())
        {
            // If the ticker is invalid, return a 400 Bad Request response to the client
            return ResponseEntity.badRequest().build();
        }
        Optional<Double> currentPrice = yahooFinanceWebScraperService.getCurrentPrice(ticker);
        if (currentPrice.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(currentPrice);
    }

    /**
     * Endpoint to retrieve the fundamental data of a specific ticker.
     *
     * @param ticker The ticker symbol for the stock.
     * @return ResponseEntity containing the current fundamental data of a ticker.
     * If the ticker is empty or null, a 400 Bad Request response is returned.
     */
    @GetMapping("finviz/fundamentaldata/{ticker}")
    public ResponseEntity<FundamentalTickerDataModel> getFundamentalData(@PathVariable String ticker)
    {
        if (ticker == null || ticker.trim().isEmpty())
        {
            // If the ticker is invalid, return a 400 Bad Request response to the client
            return ResponseEntity.badRequest().build();
        }
        FundamentalTickerDataModel fundamentalTickerData = finvizWebScraperService.getFundamentalInfo(ticker);
        if (fundamentalTickerData == null)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(fundamentalTickerData);
    }

    @GetMapping("yahoofinance/historicaldata")
    public ResponseEntity<List<HistoricalDataModel>> getHistoricalData(
            @RequestParam("ticker") String ticker,
            @RequestParam("period1") LocalDate period1,
            @RequestParam("period2") LocalDate period2,
            @RequestParam("interval") String interval
    )
    {
        if (ticker == null || ticker.trim().isEmpty())
        {
            // If the ticker is invalid, return a 400 Bad Request response to the client
            return ResponseEntity.badRequest().build();
        }
        List<HistoricalDataModel> historicalDataModels = yahooFinanceWebScraperService.getHistoricalData(ticker, period1, period2, interval);
        return ResponseEntity.ok(historicalDataModels);
    }
}
