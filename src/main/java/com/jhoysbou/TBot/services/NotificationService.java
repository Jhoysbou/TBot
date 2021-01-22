package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.NewMessageWrapper;
import com.jhoysbou.TBot.models.vkmodels.WallPostDAO;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void sendNotification(final GroupEventDAO<WallPostDAO> event);

    void newMessage(final GroupEventDAO<NewMessageWrapper> event);
}
