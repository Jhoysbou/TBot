package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.Attachment;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.*;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storages.IdStorage;
import com.jhoysbou.TBot.storages.TopicStorage;
import com.jhoysbou.TBot.utils.HashtagParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultNotificationService.class);
    private static final short ITEMS_COUNT = 200;
    private static final int MAX_PEERS_COUNT = 10;

    private final IdStorage<Long> userIdStorage;
    private final GroupApi api;
    private final HashtagParser hashtagParser;
    private final TopicStorage subscriptionStorage;
    private final String importantNotificationTag;


    @Autowired
    public DefaultNotificationService(IdStorage<Long> userIdStorage, GroupApi api,
                                      HashtagParser hashtagParser,
                                      TopicStorage subscriptionStorage,
                                      @Value("${notification.important.tag}") String importantNotificationTag) {
        this.userIdStorage = userIdStorage;
        this.api = api;
        this.hashtagParser = hashtagParser;
        this.subscriptionStorage = subscriptionStorage;
        this.importantNotificationTag = importantNotificationTag;
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

            userIdStorage.addAll(
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
    public void handleEvent(GroupEventDAO<WallPostDAO> event) {
        var text = event.getObject().getText();
        var hashtag = hashtagParser.parse(Optional.ofNullable(text));
        hashtag.ifPresent(tag -> {
            if (importantNotificationTag.equals(tag)) {
                Set<Long> peers = userIdStorage.getAllIds();
                sendNotification(event, peers);
            } else {
                Optional<Set<Long>> peersOpt = subscriptionStorage.getByTag(tag);
                peersOpt.ifPresent(peers -> {
                    if (peers.size() > 0) {
                        sendNotification(event, peers);
                    }
                });
            }

        });
    }

    private void sendNotification(GroupEventDAO<WallPostDAO> event, Set<Long> peers) {
        final Message message = new Message();
        message.setText("");
        message.setAttachments(
                List.of(
                        new Attachment(
                                "wall",
                                "-" + event.getGroup_id(),
                                String.valueOf(event.getObject().getId())
                        )
                )
        );

        try {
            var peerList = new ArrayList<>(peers);

            for (int length = peers.size(), i = 0, topIndex = MAX_PEERS_COUNT;
                 i < length;
                 i += MAX_PEERS_COUNT,
                         topIndex = Math.min((i + MAX_PEERS_COUNT), length)) {
                api.sendMessage(message, peerList.subList(i, topIndex));
            }


        } catch (IOException | InterruptedException e) {
            log.error("Couldn't send notification");
        }
    }
}
