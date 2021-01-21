package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.ConversationDAO;
import com.jhoysbou.TBot.models.vkmodels.ConversationWrapper;
import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.Pair;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storage.ConversationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultNotificationService.class);
    private static final short ITEMS_COUNT = 200;
    private final ConversationStorage storage;
    private final GroupApi api;

    @Autowired
    public DefaultNotificationService(ConversationStorage storage, GroupApi api) {
        this.storage = storage;
        this.api = api;
    }

    @PostConstruct
    void init() {
        try {
            ConversationWrapper conversations = api.getConversations(ITEMS_COUNT, 0);

            List<ConversationDAO> conversationDAOList = conversations
                    .getItems()
                    .stream()
                    .map(Pair::getConversation)
                    .collect(Collectors.toList());

            final long conversationCount = conversations.getCount();
            log.info("conversationCount = {}, items = {}", conversationCount, conversations.getItems());

            if (conversationCount > ITEMS_COUNT) {
                for (long offset = ITEMS_COUNT; offset <= conversationCount; offset += ITEMS_COUNT) {
                    conversations = api.getConversations(ITEMS_COUNT, offset);
                    conversationDAOList.addAll(
                            conversations
                                    .getItems()
                                    .stream()
                                    .map(Pair::getConversation)
                                    .collect(Collectors.toList())
                    );
                }
            }

            allowedConversations.addAll(
                    conversationDAOList
                            .stream()
                            .filter(conversationDAO -> conversationDAO.getCan_write().isAllowed())
                            .collect(Collectors.toList())
            );
        } catch (IOException | InterruptedException e) {
            log.error("Cannot fetch conversations", e);
        }
    }

    @Override
    public void sendNotification(GroupEventDAO event) {

    }
}
