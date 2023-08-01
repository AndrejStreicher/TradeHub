package com.trading.tradehub.service;

import com.trading.tradehub.util.UtilClasses;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service class to perform web scraping for stock quotes from Yahoo Finance.
 */
@Service
public class YahooFinanceWebScraperService
{

    private static final String BASE_URL = "https://finance.yahoo.com/";

    /**
     * Retrieves the current price of a stock from the HTML document.
     *
     * @param ticker The stock ticker symbol (e.g., "AAPL" for Apple Inc.).
     * @return The current stock price.
     * @throws IOException If there's an error while fetching or parsing the HTML.
     */
    public double getCurrentPrice(String ticker)
    {
        double price = 0.00;
        Document quoteDocument = UtilClasses.getHTMLFromLink(BASE_URL + "quote/" + ticker);

        Elements currentPriceElements = quoteDocument.select("fin-streamer[data-symbol=" + ticker + "]");

        Element priceElement = currentPriceElements.first();
        String priceString = priceElement.attr("value");

        try
        {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        return price;
    }
}
