package com.trading.tradehub.controller;

import com.trading.tradehub.model.ClusterInsiderBuyModel;
import com.trading.tradehub.model.FundamentalTickerDataModel;
import com.trading.tradehub.model.TickerInsiderTradeModel;
import com.trading.tradehub.service.FinvizWebScraperService;
import com.trading.tradehub.service.OpenInsiderWebScraperService;
import com.trading.tradehub.service.YahooFinanceWebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/webscrape")
public class WebScrapingController
{

    private OpenInsiderWebScraperService openInsiderWebScraperService;
    private YahooFinanceWebScraperService yahooFinanceWebScraperService;
    private FinvizWebScraperService finvizWebScraperService;

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
}
