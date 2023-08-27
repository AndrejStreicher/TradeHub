package com.trading.tradehub.service.webscraping;

import com.trading.tradehub.model.ClusterInsiderBuyModel;
import com.trading.tradehub.model.TickerInsiderTradeModel;
import com.trading.tradehub.util.UtilHTMLMethods;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service class responsible for scraping data from the OpenInsider website.
 */
@Service
public class OpenInsiderWebScraperService
{
    private static final String OPEN_INSIDER_BASE_URL = "http://openinsider.com";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Scrapes the latest cluster insider buys data from the OpenInsider website.
     *
     * @return A list of ClusterInsiderBuysModel containing the scraped data.
     * If an exception occurs during the scraping process or there is no valid data to parse,
     * an empty list is returned.
     */
    public List<ClusterInsiderBuyModel> scrapeLatestClusterBuys()
    {
        return scrapeClusterBuys(OPEN_INSIDER_BASE_URL + "/latest-cluster-buys");
    }

    public List<ClusterInsiderBuyModel> scrapeScreenedClusterBuys(String link)
    {
        return scrapeClusterBuys(link);
    }

    private List<ClusterInsiderBuyModel> scrapeClusterBuys(String link)
    {
        Document clusterBuysDoc = UtilHTMLMethods.getHTMLFromLink(link);
        assert clusterBuysDoc != null;
        Elements tinyTableClasses = clusterBuysDoc.getElementsByClass("tinytable");
        for (Element tinyTable : tinyTableClasses)
        {
            if (tinyTable.child(0).child(0).childrenSize() == 17)
            {
                return parseClusterBuys(tinyTable, dateTimeFormatter, dateFormatter);
            }
        }
        // Return an empty list if there's an exception or no data to parse
        return Collections.emptyList();
    }


    /**
     * Scrapes the latest ticker insider trades data from the OpenInsider website for a given ticker symbol.
     *
     * @param ticker The ticker symbol for which to scrape the data.
     * @return A list of TickerInsiderTradeModel containing the scraped data for the specified ticker.
     * If an exception occurs during the scraping process or there is no valid data to parse,
     * an empty list is returned.
     */
    public List<TickerInsiderTradeModel> scrapeLatestTickerClusterBuys(String ticker)
    {
        Document clusterBuysDoc = UtilHTMLMethods.getHTMLFromLink(OPEN_INSIDER_BASE_URL + "/" + ticker);
        assert clusterBuysDoc != null;
        Element subjectDetails = clusterBuysDoc.getElementById("subjectDetails");
        if (subjectDetails.child(0).text() == null)
        {
            // Return an empty list if the ticker is invalid
            return Collections.emptyList();
        }
        Elements tinyTableClasses = clusterBuysDoc.getElementsByClass("tinytable");
        for (Element tinyTable : tinyTableClasses)
        {
            if (tinyTable.child(0).child(0).childrenSize() == 16)
            {
                return parseTickerInsiderBuys(tinyTable, dateTimeFormatter, dateFormatter);
            }
        }
        // Return an empty ArrayList if there is no data to parse
        return new ArrayList<>();
    }


    /**
     * Parses the HTML elements and extracts the cluster insider buys data.
     *
     * @param tinyTable         The HTML table element containing the cluster insider buys data.
     * @param dateTimeFormatter The DateTimeFormatter for parsing date and time strings.
     * @param dateFormatter     The DateTimeFormatter for parsing date strings.
     * @return A list of ClusterInsiderBuysModel containing the parsed cluster insider buys data.
     */
    private List<ClusterInsiderBuyModel> parseClusterBuys(Element tinyTable, DateTimeFormatter dateTimeFormatter, DateTimeFormatter dateFormatter)
    {
        List<ClusterInsiderBuyModel> clusterBuys = new ArrayList<>();
        for (Element row : tinyTable.child(1).children())
        {
            Elements columns = row.select("td");
            LocalDateTime filingDate = LocalDateTime.parse(columns.get(1).text(), dateTimeFormatter);
            LocalDate tradeDate = LocalDate.parse(columns.get(2).text(), dateFormatter);
            double price = Double.parseDouble(columns.get(8).text().replace("$", ""));
            int quantity = Integer.parseInt(columns.get(9).text().replace(",", ""));
            int owned = Integer.parseInt(columns.get(10).text().replace(",", ""));
            int changeInOwned = 0;
            if (!columns.get(11).text().replace("%", "").isEmpty())
            {
                changeInOwned = Integer.parseInt(columns.get(11).text().replace("%", ""));
            }
            String stringValue = columns.get(12).text().replace("$", "");
            int value = Integer.parseInt(stringValue.replace(",", ""));
            ClusterInsiderBuyModel newClusterBuyModel = new ClusterInsiderBuyModel(columns.get(0).text(),
                    filingDate,
                    tradeDate,
                    columns.get(3).text(),
                    columns.get(4).text(),
                    columns.get(5).text(),
                    Integer.parseInt(columns.get(6).text()),
                    columns.get(7).text(),
                    price,
                    quantity,
                    owned,
                    changeInOwned,
                    value);
            clusterBuys.add(newClusterBuyModel);
        }
        return clusterBuys;
    }

    /**
     * Parses the HTML elements and extracts the ticker insider trades data.
     *
     * @param tinyTable         The HTML table element containing the ticker insider trades data.
     * @param dateTimeFormatter The DateTimeFormatter for parsing date and time strings.
     * @param dateFormatter     The DateTimeFormatter for parsing date strings.
     * @return A list of TickerInsiderTradeModel containing the parsed ticker insider trades data.
     */
    private List<TickerInsiderTradeModel> parseTickerInsiderBuys(Element tinyTable, DateTimeFormatter dateTimeFormatter, DateTimeFormatter dateFormatter)
    {
        List<TickerInsiderTradeModel> tickerInsiderBuysModels = new ArrayList<>();
        for (Element row : tinyTable.child(1).children())
        {
            Elements columns = row.select("td");
            LocalDateTime filingDate = LocalDateTime.parse(columns.get(1).text(), dateTimeFormatter);
            LocalDate tradeDate = LocalDate.parse(columns.get(2).text(), dateFormatter);
            double price = Double.parseDouble(columns.get(7).text().replace("$", ""));
            int quantity = Integer.parseInt(columns.get(8).text().replace(",", ""));
            int owned = Integer.parseInt(columns.get(9).text().replace(",", ""));
            int changeInOwned = 0;
            if (!columns.get(10).text().replace("%", "").isEmpty())
            {
                changeInOwned = Integer.parseInt(columns.get(10).text().replace("%", ""));
            }
            String stringValue = columns.get(11).text().replace("$", "");
            int value = Integer.parseInt(stringValue.replace(",", ""));
            TickerInsiderTradeModel newTickerInsiderBuyModel = new TickerInsiderTradeModel(columns.get(0).text(),
                    filingDate,
                    tradeDate,
                    columns.get(3).text(),
                    columns.get(4).text(),
                    columns.get(5).text(),
                    columns.get(6).text(),
                    price,
                    quantity,
                    owned,
                    changeInOwned,
                    value);
            tickerInsiderBuysModels.add(newTickerInsiderBuyModel);
        }
        return tickerInsiderBuysModels;
    }
}
