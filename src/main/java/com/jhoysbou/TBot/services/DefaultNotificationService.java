package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.Attachment;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.*;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storage.IdStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DefaultNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultNotificationService.class);
    private static final short ITEMS_COUNT = 10;
    private final IdStorage storage;
    private final GroupApi api;
    private final String NOTIFICATION_TAG;

    @Autowired
    public DefaultNotificationService(IdStorage storage, GroupApi api,
                                      @Value("${tbot.notification.tag}") String notification_tag) {
        this.storage = storage;
        this.api = api;
        NOTIFICATION_TAG = notification_tag;
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

            storage.addAllIds(
                    conversationDAOList
                            .stream()
                            .filter(conversationDAO -> conversationDAO.getCan_write().isAllowed())
                            .map(c -> c.getPeer().getId())
                            .collect(Collectors.toList())
            );
            log.info("Conversation storage filled");
        } catch (IOException | InterruptedException e) {
            log.error("Couldn't fetch conversations", e);
        }
    }

    @Override
    public void sendNotification(GroupEventDAO<WallPostDAO> event) {
        var text = event.getObject().getText();

        if (text.toLowerCase(Locale.ROOT).contains(NOTIFICATION_TAG)) {
            final Message message = new Message();
            message.setText("");
            message.setAttachment(
                    new Attachment(
                            "wall",
                            "-" + event.getGroup_id(),
                            String.valueOf(event.getObject().getId())
                    )
            );
            List<Long> peers = storage.getAll();

            try {
                api.sendMessage(message, peers);
            } catch (IOException | InterruptedException e) {
                log.error("Couldn't send notification");
            }
        }

    }

    @Override
    public void newMessage(GroupEventDAO<NewMessageWrapper> event) {
        storage.addId(event.getObject().getMessage().getFrom_id());
        log.debug("new id added to storage");
        log.debug("ids = {}", storage.getAll().toString());
    }
}
