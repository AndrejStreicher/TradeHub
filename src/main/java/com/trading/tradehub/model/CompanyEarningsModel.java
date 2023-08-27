package com.trading.tradehub.model;

public record CompanyEarningsModel(String symbol, String company, String eventName, String earningsCallTime,
                                   float epsEstimate, float reportedEPS, float surprise)
{
}
