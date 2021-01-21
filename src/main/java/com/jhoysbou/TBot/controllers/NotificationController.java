package com.jhoysbou.TBot.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.WallPostDAO;
import com.jhoysbou.TBot.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public String test(@RequestBody Map<String, Object> body) {
        final EventType type = EventType.valueOf(body.get("type").toString());

        switch (type) {
            case confirmation -> {
                log.info("test callback server");
                return "7e4d4c8f";
            }
            case wall_post_new -> {
                log.info("new post event");
                final ObjectMapper mapper = new ObjectMapper();
                final GroupEventDAO<WallPostDAO> event = mapper.convertValue(body, new TypeReference<GroupEventDAO<WallPostDAO>>() {});
                notificationService.sendNotification(event);
                log.debug("all good");
            }
        }

        return "ok";
    }

    private enum EventType {
        confirmation, wall_post_new
    }
}
