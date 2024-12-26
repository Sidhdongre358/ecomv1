package com.sdongre.inventory_service.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory/")
public class PingController {

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of(
                "status", "UP",
                "service", "INVENTORY-SERVICE"
        );
    }
}
