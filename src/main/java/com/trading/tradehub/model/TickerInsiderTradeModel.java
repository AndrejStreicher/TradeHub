package com.trading.tradehub.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a model for cluster insider buys data.
 */
public record TickerInsiderTradeModel(String X, LocalDateTime filingDate, LocalDate tradeDate, String ticker,
                                      String insiderName, String insiderTitle, String tradeType, double price,
                                      int quantity, int ownedShares, int changeInOwnership, int value)
{
}
