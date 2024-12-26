package com.sdongre.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class TestController {

    @GetMapping("sample")
    public String getTest() {
        return "Hello from user_service";
    }
}
