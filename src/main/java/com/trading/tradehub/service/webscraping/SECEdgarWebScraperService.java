package com.trading.tradehub.service.webscraping;

import com.trading.tradehub.util.UtilHTMLMethods;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SECEdgarWebScraperService
{
    private static final String SEC_EDGAR_ENDPOINT = "https://www.sec.gov/cgi-bin/browse-edgar";
    private static final String SEC_EDGAR_CIK_LOOKUP_ENDPOINT = "https://www.sec.gov/cgi-bin/cik_lookup?company=";

    public Map<String, String> getCompanyCIK(String companyName)
    {
        Map<String, String> cikNameMapping = new HashMap<>();
        Document companyCIKsDocument = UtilHTMLMethods.getHTMLFromLink(SEC_EDGAR_CIK_LOOKUP_ENDPOINT + companyName);
        assert companyCIKsDocument != null;
        Elements preTags = companyCIKsDocument.select("pre");
        for (Element tag : preTags)
        {
            if (tag.children().hasAttr("href"))
            {
                String[] cikCodeAndCompanyNameTexts = tag.text().split("\n");
                for (String cikCodeAndCompanyName : cikCodeAndCompanyNameTexts)
                {
                    String[] cikCodeAndCompanyNameSplit = cikCodeAndCompanyName.split(" {3}");
                    cikNameMapping.put(cikCodeAndCompanyNameSplit[0], cikCodeAndCompanyNameSplit[1]);
                }
            }
        }
        return cikNameMapping;
    }
}
