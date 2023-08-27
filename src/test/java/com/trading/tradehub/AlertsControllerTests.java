package com.trading.tradehub;

import com.trading.tradehub.controller.AlertsController;
import com.trading.tradehub.service.alerts.OpenInsiderAlertService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

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
    void getClusterBuyAlertStatus_DefaultEnabledStatusIsDisabled_StatusCode200()
    {
        ResponseEntity<Boolean> response = alertsController.getClusterBuyAlertStatus();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void updateClusterBuyAlertStatus_ClusterBuyAlertStatusIsNotSetCorrectly_StatusCode500()
    {
        boolean enabled = false;
        when(openInsiderAlertService.getClusterBuyAlertEnabled()).thenReturn(!enabled);

        ResponseEntity<Void> response = alertsController.updateClusterBuyAlertStatus(enabled);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateClusterBuyAlertStatus_ClusterBuyAlertStatusIsSetCorrectly_StatusCode200()
    {
        boolean enabled = false;
        when(openInsiderAlertService.getClusterBuyAlertEnabled()).thenReturn(enabled);

        ResponseEntity<Void> response = alertsController.updateClusterBuyAlertStatus(enabled);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
