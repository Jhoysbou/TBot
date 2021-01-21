package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.GroupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DefaultNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultNotificationService.class);

    @PostConstruct
    void init() {

    }

    @Override
    public void sendNotification(GroupEvent event) {

    }
}
