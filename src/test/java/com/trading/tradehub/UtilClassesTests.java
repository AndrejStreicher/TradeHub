package com.trading.tradehub;

import com.trading.tradehub.util.UtilHTMLMethods;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UtilClassesTests
{
    @Test
    void HTMLFileToStringTest()
    {
        String htmlString = UtilHTMLMethods.HTMLFileToString("src/test/resources/HTMLFileToStringTESTHTML.html");
        Assertions.assertNotNull(htmlString);
        Assertions.assertSame(htmlString.getClass(), String.class);
        Assertions.assertFalse(htmlString.isEmpty());
    }

    @Test
    void getHTMLFileFromLinkTest()
    {
        Document testDocument = UtilHTMLMethods.getHTMLFromLink("https://www.scrapethissite.com/pages/");
        Assertions.assertNotNull(testDocument);
    }
}
