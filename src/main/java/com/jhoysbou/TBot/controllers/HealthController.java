package com.jhoysbou.TBot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/health")
public class HealthController {
    private static final Logger log = LoggerFactory.getLogger(WebController.class);

    @GetMapping
    public ResponseEntity<String> health() {
        log.debug("health check");
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
