package com.trading.tradehub;

import com.trading.tradehub.model.ClusterInsiderBuyModel;
import com.trading.tradehub.service.alerts.OpenInsiderAlertService;
import com.trading.tradehub.service.messaging.TelegramBotService;
import com.trading.tradehub.service.webscraping.OpenInsiderWebScraperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class OpenInsiderAlertServiceTests
{
    private OpenInsiderAlertService openInsiderAlertService;
    @MockBean
    private TelegramBotService telegramBotService;
    @MockBean
    private OpenInsiderWebScraperService openInsiderWebScraperService;

    @BeforeEach
    void setUp()
    {
        openInsiderAlertService = new OpenInsiderAlertService(telegramBotService, openInsiderWebScraperService);
    }

    @Test
    void newClusterBuyAlert_NewClusterBuyAlert_TelegramMessageSent()
    {
        ClusterInsiderBuyModel mockedClusterModelInitial = Mockito.mock(ClusterInsiderBuyModel.class);
        ClusterInsiderBuyModel mockedClusterModelLatest = Mockito.mock(ClusterInsiderBuyModel.class);
        List<ClusterInsiderBuyModel> mockedClusterModelListInitial = new ArrayList<>();
        List<ClusterInsiderBuyModel> mockedClusterModelListLatest = new ArrayList<>();
        mockedClusterModelListInitial.add(mockedClusterModelInitial);
        mockedClusterModelListLatest.add(mockedClusterModelLatest);

        when(openInsiderWebScraperService.scrapeLatestClusterBuys()).thenReturn(mockedClusterModelListInitial);
        openInsiderAlertService.setClusterBuyAlertEnabled(true);
        openInsiderAlertService.newClusterBuyAlert();

        // Assertions after the first call
        verify(openInsiderWebScraperService, times(1)).scrapeLatestClusterBuys();

        // Second call to newClusterBuyAlert
        when(openInsiderWebScraperService.scrapeLatestClusterBuys()).thenReturn(mockedClusterModelListLatest);
        openInsiderAlertService.newClusterBuyAlert();

        // Assertions after the second call
        verify(openInsiderWebScraperService, times(2)).scrapeLatestClusterBuys();
        verify(telegramBotService, times(1)).sendMessage(TelegramBotService.TargetChat.GROUP, mockedClusterModelLatest);
    }
}
