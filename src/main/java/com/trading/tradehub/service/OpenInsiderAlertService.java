package com.trading.tradehub.service;

import com.trading.tradehub.model.ClusterInsiderBuysModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for alerting about new insider trade events.
 */
@Service
public class OpenInsiderAlertService
{

    // The refresh rate in milliseconds for checking new cluster buy events.
    private static final int REFRESH_RATE_IN_MILLISECONDS = 900000;
    private final TelegramBotService telegramBotService;
    private final OpenInsiderWebScraperService openInsiderWebScraperService;
    // Holds the latest cluster insider buy event data.
    private ClusterInsiderBuysModel latestClusterBuy = null;
    private boolean initialized = false;

    @Autowired
    public OpenInsiderAlertService(
            TelegramBotService telegramBotService,
            OpenInsiderWebScraperService openInsiderWebScraperService
    )
    {
        this.telegramBotService = telegramBotService;
        this.openInsiderWebScraperService = openInsiderWebScraperService;
    }

    /**
     * This method is executed periodically at the specified fixed rate to check for new cluster insider buy events.
     * It scrapes the latest cluster insider buys data and compares it with the previously stored data
     * to detect any new cluster buy events. If a new event is found, it sends an alert message using the TelegramMessageService.
     * <p>
     * Note: The method is synchronized to ensure that multiple calls do not interfere with each other when updating the shared state.
     * Any exceptions that occur during web scraping or message sending are caught and logged for error handling.
     */
    @Scheduled(fixedRate = REFRESH_RATE_IN_MILLISECONDS)
    public void newClusterBuyAlert()
    {
        try
        {
            // Get the latest cluster insider buys data from the web scraper.
            ClusterInsiderBuysModel newClusterBuyModel = openInsiderWebScraperService.scrapeLatestClusterBuys().get(0);

            // Synchronize to avoid race conditions while updating the shared latestClusterBuy field.
            synchronized (this)
            {
                // Check if the latest cluster buy is different from the previously stored one.
                // If a new cluster buy is detected, update the latestClusterBuy and send an alert message.
                if (initialized && !latestClusterBuy.equals(newClusterBuyModel))
                {
                    latestClusterBuy = newClusterBuyModel;
                    telegramBotService.sendMessage(TelegramBotService.TargetChat.GROUP, latestClusterBuy);
                }
                initialized = true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
