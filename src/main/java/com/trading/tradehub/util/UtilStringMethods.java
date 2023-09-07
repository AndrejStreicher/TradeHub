package com.trading.tradehub.util;

public class UtilStringMethods
{
    public static double parseStringDouble(String numberString)
    {
        double finalNumber = 0.0;
        numberString = numberString.replaceAll("[^0-9+.\\-TBMK]", "");
        if (numberString.contains("T"))
        {
            finalNumber = Double.parseDouble(numberString.replace("T", "")) * 1000000000000L;
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (numberString.contains("B"))
        {
            finalNumber = Double.parseDouble(numberString.replace("B", "")) * 1000000000;
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (numberString.contains("M"))
        {
            finalNumber = Double.parseDouble(numberString.replace("M", "")) * 1000000;
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (numberString.contains("K"))
        {
            finalNumber = Double.parseDouble(numberString.replace("K", "")) * 1000;
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (finalNumber == 0.0)
        {
            finalNumber = Double.parseDouble(numberString);
        }
        return finalNumber;
    }
}
