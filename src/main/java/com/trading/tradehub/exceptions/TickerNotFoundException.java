package com.trading.tradehub.exceptions;

public class TickerNotFoundException extends Exception
{
    public TickerNotFoundException(String errorMessage)
    {
        super(errorMessage);
    }
}
