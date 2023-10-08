package com.trading.tradehub.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a model for cluster insider buys data.
 */
public record ClusterInsiderBuyModel(String X, LocalDateTime filingDate, LocalDate tradeDate, String ticker,
                                     String companyName, String industry, int amountOfInsiders, String tradeType,
                                     double price, int quantity, int ownedShares, int changeInOwnedShares, long value)
{
}
