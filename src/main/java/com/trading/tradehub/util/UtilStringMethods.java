package com.trading.tradehub.util;

public class UtilStringMethods
{
    public static double parseStringDouble(String number)
    {
        double scaleFactor = Math.pow(10, 4);
        double finalNumber;
        if (number.contains("B"))
        {
            finalNumber = Double.parseDouble(number.replace("B", "")) * 1000000000;
            return Math.round(finalNumber * scaleFactor) / scaleFactor;
        }
        if (number.contains("M"))
        {
            finalNumber = Double.parseDouble(number.replace("M", "")) * 1000000;
            return Math.round(finalNumber * scaleFactor) / scaleFactor;
        }
        if (number.contains("K"))
        {
            finalNumber = Double.parseDouble(number.replace("K", "")) * 1000;
            return Math.round(finalNumber * scaleFactor) / scaleFactor;
        }
        if (number.contains("%"))
        {
            number = number.replace("%", "");
            parseStringDouble(number);
        }
        if (number.contains("(") || number.contains(")"))
        {
            finalNumber = Double.parseDouble(number.replace("(", "").replace(")", ""));
            return Math.round(finalNumber * scaleFactor) / scaleFactor;
        }
        return number.isEmpty() ? 0 : Double.parseDouble(number);
    }
}
