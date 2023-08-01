package com.trading.tradehub.service;

import com.trading.tradehub.model.ClusterInsiderBuysModel;
import com.trading.tradehub.model.TickerInsiderTradeModel;
import com.trading.tradehub.util.UtilClasses;
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
    private final String openInsiderBaseURL = "http://openinsider.com";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Scrapes the latest cluster insider buys data from the OpenInsider website.
     *
     * @return A list of ClusterInsiderBuysModel containing the scraped data.
     * If an exception occurs during the scraping process or there is no valid data to parse,
     * an empty list is returned.
     */
    public List<ClusterInsiderBuysModel> scrapeLatestClusterBuys()
    {
        Document clusterBuysDoc = UtilClasses.getHTMLFromLink(openInsiderBaseURL + "/latest-cluster-buys");
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
        Document clusterBuysDoc = UtilClasses.getHTMLFromLink(openInsiderBaseURL + "/" + ticker);
        Elements tinyTableClasses = clusterBuysDoc.getElementsByClass("tinytable");
        for (Element tinyTable : tinyTableClasses)
        {
            if (tinyTable.child(0).child(0).childrenSize() == 16)
            {
                return parseTickerInsiderBuys(tinyTable, dateTimeFormatter, dateFormatter);
            }
        }
        // Return an empty list if there's an exception or no data to parse
        return Collections.emptyList();
    }

    /**
     * Parses the HTML elements and extracts the cluster insider buys data.
     *
     * @param tinyTable         The HTML table element containing the cluster insider buys data.
     * @param dateTimeFormatter The DateTimeFormatter for parsing date and time strings.
     * @param dateFormatter     The DateTimeFormatter for parsing date strings.
     * @return A list of ClusterInsiderBuysModel containing the parsed cluster insider buys data.
     */
    private List<ClusterInsiderBuysModel> parseClusterBuys(Element tinyTable, DateTimeFormatter dateTimeFormatter, DateTimeFormatter dateFormatter)
    {
        List<ClusterInsiderBuysModel> clusterBuys = new ArrayList<>();
        for (Element row : tinyTable.child(1).children())
        {
            Elements columns = row.select("td");
            LocalDateTime filingDate = LocalDateTime.parse(columns.get(1).text(), dateTimeFormatter);
            LocalDate tradeDate = LocalDate.parse(columns.get(2).text(), dateFormatter);
            double price = Double.parseDouble(columns.get(8).text().replace("$", ""));
            int quantity = Integer.parseInt(columns.get(9).text().replace(",", ""));
            int owned = Integer.parseInt(columns.get(10).text().replace(",", ""));
            int changeInOwned = 0;
            if (!columns.get(11).text().replace("%", "").equals(""))
            {
                changeInOwned = Integer.parseInt(columns.get(11).text().replace("%", ""));
            }
            String stringValue = columns.get(12).text().replace("$", "");
            int value = Integer.parseInt(stringValue.replace(",", ""));
            ClusterInsiderBuysModel newClusterBuyModel = new ClusterInsiderBuysModel(columns.get(0).text(),
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
            if (!columns.get(10).text().replace("%", "").equals(""))
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
