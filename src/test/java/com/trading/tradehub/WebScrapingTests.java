package com.trading.tradehub;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class WebScrapingTests
{
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void latestClusterBuyWebScraperTest()
    {
        String URIString = "http://localhost:8080/webscrape/openinsider/clusterbuys";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URIString))
                .GET()
                .build();

        try
        {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertNotNull(response);
            Assertions.assertEquals(200, response.statusCode());

        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void tickerLatestInsiderBuysTest()
    {
        String URIString = "http://localhost:8080/webscrape/openinsider/AAPL";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URIString))
                .GET()
                .build();

        try
        {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertNotNull(response);
            Assertions.assertEquals(200, response.statusCode());

        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
