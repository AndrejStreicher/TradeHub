package com.trading.tradehub.controller;

import com.trading.tradehub.service.OpenInsiderAlertService;
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

    @PutMapping("/clusterbuyalert")
    public ResponseEntity<Void> updateClusterBuyAlertStatus(@RequestParam boolean enabled)
    {
        openInsiderAlertService.setClusterBuyAlertEnabled(enabled);
        if (openInsiderAlertService.getClusterBuyAlertEnabled() != enabled)
        {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/clusterbuyalert/status")
    public ResponseEntity<Boolean> getClusterBuyAlertStatus()
    {
        boolean status = openInsiderAlertService.getClusterBuyAlertEnabled();
        return ResponseEntity.ok(status);
    }
}
