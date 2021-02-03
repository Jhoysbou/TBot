package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.Attachment;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.Topic;
import com.jhoysbou.TBot.models.vkmodels.GroupEventDAO;
import com.jhoysbou.TBot.models.vkmodels.WallPostDAO;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storage.TopicStorage;
import com.jhoysbou.TBot.utils.HashtagParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DefaultNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(DefaultNotificationService.class);
    private final TopicStorage storage;
    private final GroupApi api;
    private final HashtagParser hashtagParser;
    private final TopicStorage preferenceStorage;

    @Autowired
    public DefaultNotificationService(TopicStorage storage,
                                      GroupApi api,
                                      HashtagParser hashtagParser,
                                      TopicStorage preferenceStorage) {
        this.storage = storage;
        this.api = api;
        this.hashtagParser = hashtagParser;
        this.preferenceStorage = preferenceStorage;
    }

    @Override
    public void sendNotification(GroupEventDAO<WallPostDAO> event) {
        var text = event.getObject().getText();
        var hashtag = hashtagParser.parse(Optional.ofNullable(text));
        hashtag.ifPresent(tag -> {
            Set<Long> peers = preferenceStorage.getByTag(tag);
            if (peers.size() > 0) {
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
                    api.sendMessage(message, new ArrayList<>(peers));
                } catch (IOException | InterruptedException e) {
                    log.error("Couldn't send notification");
                }
            }
        });
    }
}
