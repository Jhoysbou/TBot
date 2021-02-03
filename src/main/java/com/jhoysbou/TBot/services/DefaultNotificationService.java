package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.WallPostDAO;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storage.NewsPreferenceStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DefaultNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultNotificationService.class);
    private final NewsPreferenceStorage storage;
    private final GroupApi api;

    @Autowired
    public DefaultNotificationService(NewsPreferenceStorage storage,
                                      GroupApi api) {
        this.storage = storage;
        this.api = api;
    }

    @PostConstruct
    void init() {
//        try {
//            ConversationWrapper conversations = api.getConversations(ITEMS_COUNT, 0);
//
//            List<ConversationDAO> conversationDAOList = conversations
//                    .getItems()
//                    .stream()
//                    .map(Pair::getConversation)
//                    .collect(Collectors.toList());
//
//            final long conversationCount = conversations.getCount();
//            log.info("conversationCount = {}, items = {}", conversationCount, conversations.getItems());
//
//            if (conversationCount > ITEMS_COUNT) {
//                for (long offset = ITEMS_COUNT; offset <= conversationCount; offset += ITEMS_COUNT) {
//                    conversations = api.getConversations(ITEMS_COUNT, offset);
//                    conversationDAOList.addAll(
//                            conversations
//                                    .getItems()
//                                    .stream()
//                                    .map(Pair::getConversation)
//                                    .collect(Collectors.toList())
//                    );
//                }
//            }
//
//            storage.addAllIds(
//                    conversationDAOList
//                            .stream()
//                            .filter(conversationDAO -> conversationDAO.getCan_write().isAllowed())
//                            .map(c -> c.getPeer().getId())
//                            .collect(Collectors.toList())
//            );
//            log.info("Conversation storage filled");
//        } catch (IOException | InterruptedException e) {
//            log.error("Couldn't fetch conversations", e);
//        }
    }

    @Override
    public void sendNotification(GroupEventDAO<WallPostDAO> event) {
//        var text = event.getObject().getText();
//
//        if (text.toLowerCase(Locale.ROOT).contains(NOTIFICATION_TAG)) {
//            final Message message = new Message();
//            message.setText("");
//            message.setAttachments(
//                    List.of(
//                            new Attachment(
//                                    "wall",
//                                    "-" + event.getGroup_id(),
//                                    String.valueOf(event.getObject().getId())
//                            )
//                    )
//            );
//            List<Long> peers = storage.getAll();
//
//            try {
//                api.sendMessage(message, peers);
//            } catch (IOException | InterruptedException e) {
//                log.error("Couldn't send notification");
//            }
//        }

    }
}
