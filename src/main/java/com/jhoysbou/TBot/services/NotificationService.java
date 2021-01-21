package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.GroupEvent;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void sendNotification(final GroupEvent event);
}
