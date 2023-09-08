package com.trading.tradehub;

import com.trading.tradehub.util.UtilStringMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UtilStringMethodsTests
{
    @Test
    void parseStringDouble_ValidStringNonRoundedNumberPassed_ReturnsRoundedDouble()
    {
        double numberDouble = UtilStringMethods.parseStringDouble("56.684");
        Assertions.assertEquals(56.68, numberDouble);
    }

    @Test
    void parseStringDouble_InvalidStringPassed_ThrowsNumberFormatException()
    {
        Assertions.assertThrows(NumberFormatException.class, () -> UtilStringMethods.parseStringDouble("Hello World!"));
    }

    @Test
    void parseStringDouble_ValidStringWithReplaceableCharactersPassed_ReturnsDoubleWithoutReplaceableCharacters()
    {
        double numberDouble = UtilStringMethods.parseStringDouble("+45ABC.125");
        Assertions.assertEquals(45.13, numberDouble);

    }

}
