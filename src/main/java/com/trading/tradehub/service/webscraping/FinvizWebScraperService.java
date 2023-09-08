package com.trading.tradehub.service.webscraping;

import com.trading.tradehub.exceptions.TickerNotFoundException;
import com.trading.tradehub.model.FundamentalTickerDataModel;
import com.trading.tradehub.util.UtilHTMLMethods;
import com.trading.tradehub.util.UtilStringMethods;
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
        assert fundamentalDataDiv != null;
        return parseFundamentalData(fundamentalDataDiv.child(0));
    }

    private FundamentalTickerDataModel parseFundamentalData(Element table)
    {
        Elements tableData = table.select("td.snapshot-td2");
        String index = tableData.get(0).text();
        double priceToEarnings = UtilStringMethods.parseStringToDouble(tableData.get(1).text());
        double dilutedEarningPerShare = UtilStringMethods.parseStringToDouble(tableData.get(2).text());
        double insiderOwnership = UtilStringMethods.parseStringToDouble(tableData.get(3).text().replace("%", ""));
        double sharesOutstanding = UtilStringMethods.parseStringToDouble(tableData.get(4).text());
        double performanceWeek = UtilStringMethods.parseStringToDouble(tableData.get(5).text());
        double marketCap = UtilStringMethods.parseStringToDouble(tableData.get(6).text());
        double forwardPriceToEarnings = UtilStringMethods.parseStringToDouble(tableData.get(7).text());
        double earningPerShareEstimateNextY = UtilStringMethods.parseStringToDouble(tableData.get(8).text());
        double insiderTransactions = UtilStringMethods.parseStringToDouble(tableData.get(9).text());
        double sharesFloat = UtilStringMethods.parseStringToDouble(tableData.get(10).text());
        double performanceMonth = UtilStringMethods.parseStringToDouble(tableData.get(11).text());
        double income = UtilStringMethods.parseStringToDouble(tableData.get(12).text());
        double priceToEarningsToGrowth = UtilStringMethods.parseStringToDouble(tableData.get(13).text());
        double earningsPerShareEstimateNextQuarter = UtilStringMethods.parseStringToDouble(tableData.get(14).text());
        double institutionalOwnership = UtilStringMethods.parseStringToDouble(tableData.get(15).text());
        String[] shortFloatInterestRatio = tableData.get(16).text().split("/");
        double shortInterestShare = UtilStringMethods.parseStringToDouble(shortFloatInterestRatio[0]);
        double shortInterestShareRatio = UtilStringMethods.parseStringToDouble(shortFloatInterestRatio[1]);
        double performanceQuarter = UtilStringMethods.parseStringToDouble(tableData.get(17).text());
        double revenue = UtilStringMethods.parseStringToDouble(tableData.get(18).text());
        double priceToSales = UtilStringMethods.parseStringToDouble(tableData.get(19).text());
        double earningsPerShareGrowthThisYear = UtilStringMethods.parseStringToDouble(tableData.get(20).text());
        double institutionalTransactions = UtilStringMethods.parseStringToDouble(tableData.get(21).text());
        double shortInterest = UtilStringMethods.parseStringToDouble(tableData.get(22).text());
        double performanceHalfY = UtilStringMethods.parseStringToDouble(tableData.get(23).text());
        double bookValuePerShare = UtilStringMethods.parseStringToDouble(tableData.get(24).text());
        double priceToBook = UtilStringMethods.parseStringToDouble(tableData.get(25).text());
        double earningsPerShareGrowthNextYear = UtilStringMethods.parseStringToDouble(tableData.get(26).text());
        double returnOnAssets = UtilStringMethods.parseStringToDouble(tableData.get(27).text());
        double analystsMeanTargetPrice = UtilStringMethods.parseStringToDouble(tableData.get(28).text());
        double performanceYear = UtilStringMethods.parseStringToDouble(tableData.get(29).text());
        double cashPerShare = UtilStringMethods.parseStringToDouble(tableData.get(30).text());
        double priceToCashPerShare = UtilStringMethods.parseStringToDouble(tableData.get(31).text());
        double earningsPerShareNext5Years = UtilStringMethods.parseStringToDouble(tableData.get(32).text());
        double returnOnEquity = UtilStringMethods.parseStringToDouble(tableData.get(33).text());
        String[] lowHigh52Week = tableData.get(34).text().split("-");
        double low52Week = UtilStringMethods.parseStringToDouble(lowHigh52Week[0]);
        double high52Week = UtilStringMethods.parseStringToDouble(lowHigh52Week[1]);
        double performanceYearToDate = UtilStringMethods.parseStringToDouble(tableData.get(35).text());
        double annualDividend = UtilStringMethods.parseStringToDouble(tableData.get(36).text());
        double priceToFreeCashFlowAfterDividends = UtilStringMethods.parseStringToDouble(tableData.get(37).text());
        double earningsPerShareGrowthPast5Years = UtilStringMethods.parseStringToDouble(tableData.get(38).text());
        double returnOnInvestment = UtilStringMethods.parseStringToDouble(tableData.get(39).text());
        double distanceFrom52WeekHigh = UtilStringMethods.parseStringToDouble(tableData.get(40).text());
        double beta = UtilStringMethods.parseStringToDouble(tableData.get(41).text());
        double dividendPercent = UtilStringMethods.parseStringToDouble(tableData.get(42).text().replace("%", ""));
        double quickRatio = UtilStringMethods.parseStringToDouble(tableData.get(43).text());
        double salesPast5Years = UtilStringMethods.parseStringToDouble(tableData.get(44).text());
        double grossMargin = UtilStringMethods.parseStringToDouble(tableData.get(45).text());
        double distanceFrom52WeekLow = UtilStringMethods.parseStringToDouble(tableData.get(46).text());
        double averageTrueRange = UtilStringMethods.parseStringToDouble(tableData.get(47).text());
        int employees = Integer.parseInt(tableData.get(48).text());
        double currentRatio = UtilStringMethods.parseStringToDouble(tableData.get(49).text());
        double quarterlyRevenueGrowth = UtilStringMethods.parseStringToDouble(tableData.get(50).text());
        double operatingMargin = UtilStringMethods.parseStringToDouble(tableData.get(51).text());
        double relativeStrengthIndex14 = UtilStringMethods.parseStringToDouble(tableData.get(52).text());
        String[] volatilityWeekMonth = tableData.get(53).text().split(" ");
        double volatilityWeek = UtilStringMethods.parseStringToDouble(volatilityWeekMonth[0]);
        double volatilityMonth = UtilStringMethods.parseStringToDouble(volatilityWeekMonth[1]);
        boolean optionable = tableData.get(54).text().equals("Yes");
        double debtToEquity = UtilStringMethods.parseStringToDouble(tableData.get(55).text());
        double quarterlyEarningsGrowth = UtilStringMethods.parseStringToDouble(tableData.get(56).text());
        double profitMargin = UtilStringMethods.parseStringToDouble(tableData.get(57).text());
        double relativeVolume = UtilStringMethods.parseStringToDouble(tableData.get(58).text());
        double previousClose = UtilStringMethods.parseStringToDouble(tableData.get(59).text());
        boolean shortable = tableData.get(60).text().equals("Yes");
        double longTermDebtToEquity = UtilStringMethods.parseStringToDouble(tableData.get(61).text());
        MonthDay earningsDate = MonthDay.parse(tableData.get(62).text().replace("AMC", "").replace("BMC", "").trim(), new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM dd")
                .toFormatter());
        boolean earningDateAfterMarketClose = tableData.get(62).text().contains("AMC");
        double dividendPayoutRatio = UtilStringMethods.parseStringToDouble(tableData.get(63).text());
        double averageVolume = UtilStringMethods.parseStringToDouble(tableData.get(64).text());
        double price = UtilStringMethods.parseStringToDouble(tableData.get(65).text());
        double analystsMeanRecommendation = UtilStringMethods.parseStringToDouble(tableData.get(66).text());
        double simpleMovingAverage20 = UtilStringMethods.parseStringToDouble(tableData.get(67).text());
        double simpleMovingAverage50 = UtilStringMethods.parseStringToDouble(tableData.get(68).text());
        double simpleMovingAverage200 = UtilStringMethods.parseStringToDouble(tableData.get(69).text());
        int volume = Integer.parseInt(tableData.get(70).text().replace(",", ""));
        double performanceToday = UtilStringMethods.parseStringToDouble(tableData.get(71).text());
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

}
