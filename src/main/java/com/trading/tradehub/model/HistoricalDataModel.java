package com.trading.tradehub.model;

import java.time.LocalDate;

public record HistoricalDataModel(LocalDate date, double open, double high, double low, double close,
                                  double adjustedClose, int volume)
{
}
