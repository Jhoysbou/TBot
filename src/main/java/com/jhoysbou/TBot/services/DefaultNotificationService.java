package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.ConversationDAO;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;
import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class DefaultNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultNotificationService.class);
    private final GroupApi api;

    @Autowired
    public DefaultNotificationService(GroupApi api) {
        this.api = api;
    }

    @PostConstruct
    void init() {
        try {
            final ConversationWrapper conversations = api.getConversations((short) 200, 0);
            final long conversationCount = conversations.getCount();
            log.info("got conversations");


        } catch (IOException | InterruptedException e) {
            log.error("Cannot fetch conversations", e);
        }
    }

    @Override
    public void sendNotification(GroupEventDAO event) {

    }
}
