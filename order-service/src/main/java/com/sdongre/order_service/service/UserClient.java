package com.sdongre.order_service.service;


import com.sdongre.order_service.model.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "userClient", url = "http://localhost:8088")
public interface UserClient {

    @GetMapping("/api/manager/user/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
