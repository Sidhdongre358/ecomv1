package com.sdongre.product_service.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ping/product_service")
public class PingController {

    @GetMapping
    public Map<String, String> ping() {
        return Map.of(
                "status", "UP",
                "service", "PRODUCT-SERVICE"
        );
    }
}
