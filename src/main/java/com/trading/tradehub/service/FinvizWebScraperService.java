package com.trading.tradehub.service;

import com.trading.tradehub.exceptions.TickerNotFoundException;
import com.trading.tradehub.model.FundamentalTickerDataModel;
import com.trading.tradehub.util.UtilHTMLMethods;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.MonthDay;
import java.time.format.DateTimeFormatterBuilder;

@Service
public class FinvizWebScraperService
{
    private static final String BASE_URL = "https://finviz.com/";
    private final Logger logger = LoggerFactory.getLogger(FinvizWebScraperService.class);

    public FundamentalTickerDataModel getFundamentalInfo(String ticker)
    {
        Document finvizDocument = null;
        try
        {
            finvizDocument = UtilHTMLMethods.getHTMLFromLink(BASE_URL + "quote.ashx?t=" + ticker + "&p=d");
            assert finvizDocument != null;
            if (finvizDocument.location().contains("search"))
            {
                throw new TickerNotFoundException("Ticker not found: " + ticker);
            }
        } catch (TickerNotFoundException e)
        {
            logger.error("Ticker not found!", e);
        }
        Element fundamentalDataDiv = finvizDocument.select("div.snapshot-table-wrapper").first();
        return parseFundamentalData(fundamentalDataDiv.child(0));
    }

    private FundamentalTickerDataModel parseFundamentalData(Element table)
    {
        Elements tableData = table.select("td.snapshot-td2");
        String index = tableData.get(0).text();
        double priceToEarnings = parseStringDouble(tableData.get(1).text());
        double dilutedEarningPerShare = parseStringDouble(tableData.get(2).text());
        double insiderOwnership = parseStringDouble(tableData.get(3).text().replace("%", ""));
        double sharesOutstanding = parseStringDouble(tableData.get(4).text());
        double performanceWeek = parseStringDouble(tableData.get(5).text());
        double marketCap = parseStringDouble(tableData.get(6).text());
        double forwardPriceToEarnings = parseStringDouble(tableData.get(7).text());
        double earningPerShareEstimateNextY = parseStringDouble(tableData.get(8).text());
        double insiderTransactions = parseStringDouble(tableData.get(9).text());
        double sharesFloat = parseStringDouble(tableData.get(10).text());
        double performanceMonth = parseStringDouble(tableData.get(11).text());
        double income = parseStringDouble(tableData.get(12).text());
        double priceToEarningsToGrowth = parseStringDouble(tableData.get(13).text());
        double earningsPerShareEstimateNextQuarter = parseStringDouble(tableData.get(14).text());
        double institutionalOwnership = parseStringDouble(tableData.get(15).text());
        String[] shortFloatInterestRatio = tableData.get(16).text().split("/");
        double shortInterestShare = parseStringDouble(shortFloatInterestRatio[0]);
        double shortInterestShareRatio = parseStringDouble(shortFloatInterestRatio[1]);
        double performanceQuarter = parseStringDouble(tableData.get(17).text());
        double revenue = parseStringDouble(tableData.get(18).text());
        double priceToSales = parseStringDouble(tableData.get(19).text());
        double earningsPerShareGrowthThisYear = parseStringDouble(tableData.get(20).text());
        double institutionalTransactions = parseStringDouble(tableData.get(21).text());
        double shortInterest = parseStringDouble(tableData.get(22).text());
        double performanceHalfY = parseStringDouble(tableData.get(23).text());
        double bookValuePerShare = parseStringDouble(tableData.get(24).text());
        double priceToBook = parseStringDouble(tableData.get(25).text());
        double earningsPerShareGrowthNextYear = parseStringDouble(tableData.get(26).text());
        double returnOnAssets = parseStringDouble(tableData.get(27).text());
        double analystsMeanTargetPrice = parseStringDouble(tableData.get(28).text());
        double performanceYear = parseStringDouble(tableData.get(29).text());
        double cashPerShare = parseStringDouble(tableData.get(30).text());
        double priceToCashPerShare = parseStringDouble(tableData.get(31).text());
        double earningsPerShareNext5Years = parseStringDouble(tableData.get(32).text());
        double returnOnEquity = parseStringDouble(tableData.get(33).text());
        String[] lowHigh52Week = tableData.get(34).text().split("-");
        double low52Week = parseStringDouble(lowHigh52Week[0]);
        double high52Week = parseStringDouble(lowHigh52Week[1]);
        double performanceYearToDate = parseStringDouble(tableData.get(35).text());
        double annualDividend = parseStringDouble(tableData.get(36).text());
        double priceToFreeCashFlowAfterDividends = parseStringDouble(tableData.get(37).text());
        double earningsPerShareGrowthPast5Years = parseStringDouble(tableData.get(38).text());
        double returnOnInvestment = parseStringDouble(tableData.get(39).text());
        double distanceFrom52WeekHigh = parseStringDouble(tableData.get(40).text());
        double beta = parseStringDouble(tableData.get(41).text());
        double dividendPercent = parseStringDouble(tableData.get(42).text().replace("%", ""));
        double quickRatio = parseStringDouble(tableData.get(43).text());
        double salesPast5Years = parseStringDouble(tableData.get(44).text());
        double grossMargin = parseStringDouble(tableData.get(45).text());
        double distanceFrom52WeekLow = parseStringDouble(tableData.get(46).text());
        double averageTrueRange = parseStringDouble(tableData.get(47).text());
        int employees = Integer.parseInt(tableData.get(48).text());
        double currentRatio = parseStringDouble(tableData.get(49).text());
        double quarterlyRevenueGrowth = parseStringDouble(tableData.get(50).text());
        double operatingMargin = parseStringDouble(tableData.get(51).text());
        double relativeStrengthIndex14 = parseStringDouble(tableData.get(52).text());
        String[] volatilityWeekMonth = tableData.get(53).text().split(" ");
        double volatilityWeek = parseStringDouble(volatilityWeekMonth[0]);
        double volatilityMonth = parseStringDouble(volatilityWeekMonth[1]);
        boolean optionable = tableData.get(54).text().equals("Yes");
        double debtToEquity = parseStringDouble(tableData.get(55).text());
        double quarterlyEarningsGrowth = parseStringDouble(tableData.get(56).text());
        double profitMargin = parseStringDouble(tableData.get(57).text());
        double relativeVolume = parseStringDouble(tableData.get(58).text());
        double previousClose = parseStringDouble(tableData.get(59).text());
        boolean shortable = tableData.get(60).text().equals("Yes");
        double longTermDebtToEquity = parseStringDouble(tableData.get(61).text());
        MonthDay earningsDate = MonthDay.parse(tableData.get(62).text().replace("AMC", "").replace("BMC", "").trim(), new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM dd")
                .toFormatter());
        boolean earningDateAfterMarketClose = tableData.get(62).text().contains("AMC");
        double dividendPayoutRatio = parseStringDouble(tableData.get(63).text());
        double averageVolume = parseStringDouble(tableData.get(64).text());
        double price = parseStringDouble(tableData.get(65).text());
        double analystsMeanRecommendation = parseStringDouble(tableData.get(66).text());
        double simpleMovingAverage20 = parseStringDouble(tableData.get(67).text());
        double simpleMovingAverage50 = parseStringDouble(tableData.get(68).text());
        double simpleMovingAverage200 = parseStringDouble(tableData.get(69).text());
        int volume = Integer.parseInt(tableData.get(70).text().replace(",", ""));
        double performanceToday = parseStringDouble(tableData.get(71).text());
        return new FundamentalTickerDataModel(
                index,
                priceToEarnings,
                dilutedEarningPerShare,
                insiderOwnership,
                sharesOutstanding,
                performanceWeek,
                marketCap,
                forwardPriceToEarnings,
                earningPerShareEstimateNextY,
                insiderTransactions,
                sharesFloat,
                performanceMonth,
                income,
                priceToEarningsToGrowth,
                earningsPerShareEstimateNextQuarter,
                institutionalOwnership,
                shortInterestShare,
                shortInterestShareRatio,
                performanceQuarter,
                revenue,
                priceToSales,
                earningsPerShareGrowthThisYear,
                institutionalTransactions,
                shortInterest,
                performanceHalfY,
                bookValuePerShare,
                priceToBook,
                earningsPerShareGrowthNextYear,
                returnOnAssets,
                analystsMeanTargetPrice,
                performanceYear,
                cashPerShare,
                priceToCashPerShare,
                earningsPerShareNext5Years,
                returnOnEquity,
                low52Week,
                high52Week,
                performanceYearToDate,
                annualDividend,
                priceToFreeCashFlowAfterDividends,
                earningsPerShareGrowthPast5Years,
                returnOnInvestment,
                distanceFrom52WeekHigh,
                beta,
                dividendPercent,
                quickRatio,
                salesPast5Years,
                grossMargin,
                distanceFrom52WeekLow,
                averageTrueRange,
                employees,
                currentRatio,
                quarterlyRevenueGrowth,
                operatingMargin,
                relativeStrengthIndex14,
                volatilityWeek,
                volatilityMonth,
                optionable,
                debtToEquity,
                quarterlyEarningsGrowth,
                profitMargin,
                relativeVolume,
                previousClose,
                shortable,
                longTermDebtToEquity,
                earningsDate,
                earningDateAfterMarketClose,
                dividendPayoutRatio,
                averageVolume,
                price,
                analystsMeanRecommendation,
                simpleMovingAverage20,
                simpleMovingAverage50,
                simpleMovingAverage200,
                volume,
                performanceToday
        );

    }

    private double parseStringDouble(String number)
    {
        double scaleFactor = Math.pow(10, 4);
        double finalNumber;
        if (number.contains("B"))
        {
            finalNumber = Double.parseDouble(number.replace("B", "")) * 1000000000;
            return Math.round(finalNumber * scaleFactor) / scaleFactor;
        }
        if (number.contains("M"))
        {
            finalNumber = Double.parseDouble(number.replace("M", "")) * 1000000;
            return Math.round(finalNumber * scaleFactor) / scaleFactor;
        }
        if (number.contains("K"))
        {
            finalNumber = Double.parseDouble(number.replace("K", "")) * 1000;
            return Math.round(finalNumber * scaleFactor) / scaleFactor;
        }
        if (number.contains("%"))
        {
            finalNumber = Double.parseDouble(number.replace("%", "")) / 100;
            return Math.round(finalNumber * scaleFactor) / scaleFactor;
        }
        return number.isEmpty() ? 0 : Double.parseDouble(number);
    }
}
