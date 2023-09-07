package com.trading.tradehub.model;

import java.util.Optional;

public record TickerSummaryModel(String ticker, String companyName, String marketCap, boolean isOpen,
                                 Optional<Double> currentPrice,
                                 double changeSinceOpen, double changeSinceOpenPercent, double fiftyTwoWeekLow,
                                 double fiftyTwoWeekHigh, int volume, int averageVolume)
{
}
