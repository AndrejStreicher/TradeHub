package com.trading.tradehub.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilStringMethods
{
    private UtilStringMethods()
    {

    }

    private static final Logger logger = LoggerFactory.getLogger(UtilStringMethods.class);

    public static double parseStringToDouble(String numberString)
    {
        double finalNumber = 0.0;
        numberString = numberString.replaceAll("[^0-9+.\\-]", "");
        try
        {
            finalNumber = Double.parseDouble(numberString);
        } catch (NullPointerException e)
        {
            logger.error(String.format("Input %s is not a valid string representation of a number.", numberString));
        }

        finalNumber = Math.round(finalNumber * Math.pow(10, 2)) / Math.pow(10, 2);
        return finalNumber;
    }

    public static String parseDoubleToString(double numberDouble)
    {
        if (numberDouble == (long) numberDouble)
        {
            return String.format("%.0f", numberDouble);
        } else
        {
            return String.format("%.2f", numberDouble);
        }
    }
}
