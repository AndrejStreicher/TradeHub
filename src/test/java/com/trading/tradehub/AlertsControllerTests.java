package com.trading.tradehub;

import com.trading.tradehub.controller.AlertsController;
import com.trading.tradehub.service.OpenInsiderAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

@WebMvcTest(AlertsController.class)
class AlertsControllerTests
{

    @MockBean
    private OpenInsiderAlertService openInsiderAlertService;
    private AlertsController alertsController;

    @BeforeEach
    public void setUp()
    {
        alertsController = new AlertsController(openInsiderAlertService);
    }

    @Test
    void updateClusterBuyAlertStatus_ClusterBuyAlertSetToEnabled_StatusIs200()
    {
        boolean enabled = true;

        ResponseEntity<Void> actualResponse = alertsController.updateClusterBuyAlertStatus(enabled);

        verify(openInsiderAlertService).setClusterBuyAlertEnabled(enabled);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    void updateClusterBuyAlertStatus_ClusterBuyAlertSetToDisabled_StatusIs200()
    {
        boolean enabled = false;

        ResponseEntity<Void> actualResponse = alertsController.updateClusterBuyAlertStatus(enabled);

        verify(openInsiderAlertService).setClusterBuyAlertEnabled(enabled);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    void getClusterBuyAlertStatus_DefaultEnabledStatus_StatusIs200()
    {
        ResponseEntity<Boolean> actualResponseFalse = alertsController.getClusterBuyAlertStatus();
        assertEquals(HttpStatus.OK, actualResponseFalse.getStatusCode());
        assertFalse(actualResponseFalse.getBody());
    }
}
