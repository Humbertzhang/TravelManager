package com.example.travelmanager.controller;

import com.example.travelmanager.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import response.TokenResponse;

@RestController
@RequestMapping("/api")
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @Autowired
    private UserDao userDao;

    @PostMapping("/token/")
    public HttpEntity addUser(@RequestParam String userName, @RequestParam String name)
    {
        TokenResponse tokenResponse = new TokenResponse();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(tokenResponse);
    }
}
