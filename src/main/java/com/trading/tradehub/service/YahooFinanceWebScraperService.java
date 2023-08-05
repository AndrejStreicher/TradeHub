package com.trading.tradehub.service;

import com.trading.tradehub.model.HistoricalDataModel;
import com.trading.tradehub.util.UtilHTMLMethods;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class to perform web scraping for stock quotes from Yahoo Finance.
 */
@Service
public class YahooFinanceWebScraperService
{
    private static final String BASE_URL = "https://finance.yahoo.com/";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    /**
     * Retrieves the current price of a stock from the HTML document.
     *
     * @param ticker The stock ticker symbol (e.g., "AAPL" for Apple Inc.).
     * @return The current stock price.
     * @throws NumberFormatException If there's an error while fetching or parsing the HTML.
     */
    public Optional<Double> getCurrentPrice(String ticker)
    {
        Document quoteDocument = UtilHTMLMethods.getHTMLFromLink(BASE_URL + "quote/" + ticker);
        if (quoteDocument.location().contains("lookup"))
        {
            return Optional.empty();
        }
        Elements currentPriceElements = quoteDocument.select("fin-streamer[data-symbol=" + ticker + "]");

        Element priceElement = currentPriceElements.first();
        String priceString = priceElement.attr("value");

        return Optional.of(Double.parseDouble(priceString));
    }

    public List<HistoricalDataModel> getHistoricalData(String ticker, LocalDate period1, LocalDate period2, String interval)
    {
        List<HistoricalDataModel> historicalDataModels = new ArrayList<>();
        long period1Unix = period1.toEpochSecond(LocalTime.parse("12:00:00"), ZoneOffset.of("Z"));
        long period2Unix = period2.toEpochSecond(LocalTime.parse("12:00:00"), ZoneOffset.of("Z"));
        Document historicalDataDocument = UtilHTMLMethods.getHTMLFromLink(
                BASE_URL +
                        "quote/" +
                        ticker +
                        "/history?period1=" +
                        period1Unix +
                        "&period2=" +
                        period2Unix +
                        "&interval=" +
                        interval +
                        "&filter=history&frequency=" +
                        interval +
                        "&includeAdjustedClose=true");
        assert historicalDataDocument != null;
        Elements historicalDataElements = historicalDataDocument.select("table[data-test=historical-prices]");
        Element historicalDataElement = historicalDataElements.first();
        for (Element row : historicalDataElement.child(1).children())
        {
            Elements columns = row.select("td");
            LocalDate date = LocalDate.parse(columns.get(0).text(), dateTimeFormatter);
            double open = Double.parseDouble(columns.get(1).text());
            double high = Double.parseDouble(columns.get(2).text());
            double low = Double.parseDouble(columns.get(3).text());
            double close = Double.parseDouble(columns.get(4).text());
            double adjustedClose = Double.parseDouble(columns.get(5).text());
            int volume = Integer.parseInt(columns.get(6).text().replace(",", ""));
            HistoricalDataModel newHistoricalDataModel = new HistoricalDataModel(date, open, high, low, close, adjustedClose, volume);
            historicalDataModels.add(newHistoricalDataModel);
        }
        return historicalDataModels;
    }
}
