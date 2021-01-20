package com.jhoysbou.TBot.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @PostMapping
    public String test() {
        return "7e4d4c8f";
    }


}
