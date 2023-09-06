package com.trading.tradehub.service.webscraping;

import com.trading.tradehub.model.HistoricalDataModel;
import com.trading.tradehub.model.TickerSummaryModel;
import com.trading.tradehub.util.UtilHTMLMethods;
import com.trading.tradehub.util.UtilStringMethods;
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
    public Document getYahooFinanceQuoteDocument(String ticker)
    {
        return UtilHTMLMethods.getHTMLFromLink(BASE_URL + "quote/" + ticker);
    }

    public Optional<Double> getCurrentPrice(String ticker)
    {
        Document quoteDocument = getYahooFinanceQuoteDocument(ticker);
        assert quoteDocument != null;
        return getPriceFromDocument(quoteDocument, ticker);
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
        assert historicalDataElement != null;
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

    private Optional<Double> getPriceFromDocument(Document document, String ticker)
    {
        if (document.location().contains("lookup"))
        {
            return Optional.empty();
        }
        Elements currentPriceElements = document.select("fin-streamer[data-symbol=" + ticker + "]");

        Element priceElement = currentPriceElements.first();
        assert priceElement != null;
        String priceString = priceElement.attr("value");

        return Optional.of(Double.parseDouble(priceString));
    }

    private String[] getStockNameAndTickerFromDocument(Document document)
    {
        if (document.location().contains("lookup"))
        {
            return new String[0];
        }
        Elements h1Element = document.getElementsByClass("D(ib) Fz(18px)");
        String[] stockNameAndTickerArray = h1Element.text().split("[()]");
        stockNameAndTickerArray[0] = stockNameAndTickerArray[0].trim();
        stockNameAndTickerArray[1] = stockNameAndTickerArray[1].trim();
        return stockNameAndTickerArray;
    }

    public TickerSummaryModel getTickerSummary(String ticker)
    {
        Document stockSummaryDocument = getYahooFinanceQuoteDocument(ticker);
        String[] stockNameAndTicker = getStockNameAndTickerFromDocument(stockSummaryDocument);
        Element stockSummaryRows = stockSummaryDocument.getElementById("quote-summary");
        assert stockSummaryRows != null;

        String marketCapStr = stockSummaryRows.select("[data-test=MARKET_CAP-value]").text();
        double marketCap = "N/A".equals(marketCapStr) || marketCapStr.isEmpty() ? 0.0 : UtilStringMethods.parseStringDouble(marketCapStr);

        boolean isOpen = stockSummaryRows.getElementsByClass("C($tertiaryColor) D(b) Fz(12px) Fw(n) Mstart(0)--mobpsm Mt(6px)--mobpsm Whs(n)")
                .text()
                .contains("open");

        Optional<Double> currentPrice = getPriceFromDocument(stockSummaryDocument, ticker);

        String changeSinceOpenStr = stockSummaryDocument.select("[data-test=qsp-price-change]").text();
        double changeSinceOpen = "N/A".equals(changeSinceOpenStr) || changeSinceOpenStr.isEmpty() ? 0.0 : Double.parseDouble(changeSinceOpenStr);

        String changeSinceOpenPercentStr = stockSummaryDocument.select("[data-field=regularMarketChangePercent][data-symbol=SYM]").text();
        double changeSinceOpenPercent = "N/A".equals(changeSinceOpenPercentStr) || changeSinceOpenPercentStr.isEmpty() ? 0.0 : UtilStringMethods.parseStringDouble(changeSinceOpenPercentStr);

        String[] fiftyTwoWeekRange = stockSummaryRows.select("[data-test=FIFTY_TWO_WK_RANGE-value]").text().split("-");
        double fiftyTwoWeekRangeLow = fiftyTwoWeekRange[0].isEmpty() ? 0.0 : Double.parseDouble(fiftyTwoWeekRange[0]);
        double fiftyTwoWeekRangeHigh = fiftyTwoWeekRange.length == 1 || fiftyTwoWeekRange[1].isEmpty() ? 0.0 : Double.parseDouble(fiftyTwoWeekRange[1]);
        String volumeString = stockSummaryRows.select("[data-field=regularMarketVolume]").text();
        int volume = "N/A".equals(volumeString) || volumeString.isEmpty() ? 0 : Integer.parseInt(volumeString.replace(",", ""));

        String averageVolumeString = stockSummaryRows.select("[data-test=AVERAGE_VOLUME_3MONTH-value]").text();
        int averageVolume = "N/A".equals(averageVolumeString) || averageVolumeString.isEmpty() ? 0 : Integer.parseInt(averageVolumeString.replace(",", ""));

        return new TickerSummaryModel(
                stockNameAndTicker[1],
                stockNameAndTicker[0],
                marketCap,
                isOpen,
                currentPrice,
                changeSinceOpen,
                changeSinceOpenPercent,
                fiftyTwoWeekRangeLow,
                fiftyTwoWeekRangeHigh,
                volume,
                averageVolume
        );
    }

}
