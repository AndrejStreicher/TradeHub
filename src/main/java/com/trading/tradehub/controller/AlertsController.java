package com.trading.tradehub.controller;

import com.trading.tradehub.service.alerts.OpenInsiderAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
public class AlertsController
{

    private final OpenInsiderAlertService openInsiderAlertService;

    @Autowired
    public AlertsController(OpenInsiderAlertService openInsiderAlertService)
    {
        this.openInsiderAlertService = openInsiderAlertService;
    }

    /**
     * Endpoint to turn on or off the cluster buy alert.
     *
     * @param enabled The boolean that represents the required status of the alert.
     * @return ResponseEntity that returns a 200 OK response if the alert status corresponds to the requested boolean.
     * If the status doesn't correspond to the requested boolean i.e. it wasn't changed, a 500 Internal server error
     * response is returned.
     */

    @PutMapping("/clusterbuyalert")
    public ResponseEntity<Void> updateClusterBuyAlertStatus(@RequestBody boolean enabled)
    {
        openInsiderAlertService.setClusterBuyAlertEnabled(enabled);
        if (openInsiderAlertService.getClusterBuyAlertEnabled() != enabled)
        {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to retrieve the current status of the cluster buy alert.
     *
     * @return ResponseEntity containing the current status of the alert. True for enabled, false for disabled.
     */

    @GetMapping("/clusterbuyalert/status")
    public ResponseEntity<Boolean> getClusterBuyAlertStatus()
    {
        boolean status = openInsiderAlertService.getClusterBuyAlertEnabled();
        return ResponseEntity.ok(status);
    }
}
