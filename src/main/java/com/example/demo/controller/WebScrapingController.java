package com.example.demo.controller;

import com.example.demo.model.ClusterInsiderBuysModel;
import com.example.demo.model.TickerInsiderTradeModel;
import com.example.demo.service.OpenInsiderWebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webscrape")
public class WebScrapingController
{

    private final OpenInsiderWebScraperService openInsiderWebScraperService;

    @Autowired
    public WebScrapingController(OpenInsiderWebScraperService openInsiderWebScraperService)
    {
        this.openInsiderWebScraperService = openInsiderWebScraperService;
    }

    /**
     * Endpoint to retrieve the latest cluster insider buys.
     *
     * @return ResponseEntity containing a list of ClusterInsiderBuysModel if available, else a 404 Not Found response.
     */
    @GetMapping("openinsider/clusterbuys")
    public ResponseEntity<List<ClusterInsiderBuysModel>> getClusterBuys()
    {
        List<ClusterInsiderBuysModel> clusterBuys = openInsiderWebScraperService.scrapeLatestClusterBuys();
        if (clusterBuys.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clusterBuys);
    }

    /**
     * Endpoint to retrieve the latest insider buys for a specific ticker.
     *
     * @param ticker The ticker symbol for the stock.
     * @return ResponseEntity containing a list of TickerInsiderBuysModel if available, else a 404 Not Found response.
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
        // Return the response with the list of TickerInsiderBuysModel
        return ResponseEntity.ok(insiderTrades);
    }

}
