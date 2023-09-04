package com.trading.tradehub.model;

import java.util.Optional;

public record TickerSummaryModel(String ticker, String companyName, double marketCap, boolean isOpen,
                                 Optional<Double> currentPrice,
                                 double changeSinceOpen, double changeSinceOpenPercent, double fiftyTwoWeekLow,
                                 double fiftyTwoWeekHigh, double volume, double averageVolume)
{
}
