package com.trading.tradehub.util;

public class UtilStringMethods
{
    private UtilStringMethods()
    {

    }

    public static double parseStringDouble(String number)
    {
        double finalNumber = 0.0;
        if (number.contains("T"))
        {
            finalNumber = Double.parseDouble(number.replace("T", "")) * 1000000000000L;
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (number.contains("B"))
        {
            finalNumber = Double.parseDouble(number.replace("B", "")) * 1000000000;
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (number.contains("M"))
        {
            finalNumber = Double.parseDouble(number.replace("M", "")) * 1000000;
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (number.contains("K"))
        {
            finalNumber = Double.parseDouble(number.replace("K", "")) * 1000;
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (number.contains("%"))
        {
            finalNumber = Double.parseDouble(number.replace("%", ""));
            parseStringDouble(String.valueOf(finalNumber));
        }
        if (number.contains("(") || number.contains(")"))
        {
            finalNumber = Double.parseDouble(number.replace("(", "").replace(")", ""));
            parseStringDouble(String.valueOf(finalNumber));
        }
        return finalNumber;
    }
}
