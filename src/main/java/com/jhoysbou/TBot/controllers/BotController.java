package com.jhoysbou.TBot.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.NewMessageWrapper;
import com.jhoysbou.TBot.models.vkmodels.WallPostDAO;
import com.jhoysbou.TBot.services.FAQService;
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
public class BotController {
    private static final Logger log = LoggerFactory.getLogger(BotController.class);
    private final NotificationService notificationService;
    private final FAQService faqService;

    @Autowired
    public BotController(NotificationService notificationService, FAQService faqService) {
        this.notificationService = notificationService;
        this.faqService = faqService;
    }

    @PostMapping
    public String newEvent(@RequestBody Map<String, Object> body) {
        final EventType type = EventType.valueOf(body.get("type").toString());
        final ObjectMapper mapper = new ObjectMapper();

        switch (type) {
            case confirmation -> {
                log.info("test callback server");
                return "5cb7439f";
            }
            case wall_post_new -> {
                log.info("new post");
                final GroupEventDAO<WallPostDAO> event = mapper.convertValue(
                        body,
                        new TypeReference<GroupEventDAO<WallPostDAO>>() {
                        }
                );
                notificationService.sendNotification(event);
                log.debug("all good");
            }
            case message_new -> {
                log.info("new message");
                final GroupEventDAO<NewMessageWrapper> event = mapper.convertValue(
                        body,
                        new TypeReference<GroupEventDAO<NewMessageWrapper>>() {
                        }
                );
                notificationService.newMessage(event);
                faqService.handleMessage(event);
            }
        }

        return "ok";
    }

    private enum EventType {
        confirmation, wall_post_new, message_new
    }
}
