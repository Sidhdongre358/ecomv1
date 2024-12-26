package com.sdongre.favourite_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favourites/")
public class TestController {

    @GetMapping("/")
    public String getTest() {
        return "Hello from favourite_service";
    }
}
