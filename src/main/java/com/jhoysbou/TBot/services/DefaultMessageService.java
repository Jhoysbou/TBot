package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.MenuAttachmentsDto;
import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.*;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storage.MenuStorage;
import com.jhoysbou.TBot.storage.TopicStorage;
import com.jhoysbou.TBot.utils.HashtagParser;
import com.jhoysbou.TBot.utils.ServicePreferenceTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DefaultMessageService implements MessageService {
    private static final Logger log = LoggerFactory.getLogger(DefaultMessageService.class);

    private final GroupApi api;
    private final MenuStorage menuStorage;
    private final HashtagParser hashtagParser;
    private final TopicStorage preferenceStorage;

    @Autowired
    public DefaultMessageService(GroupApi api,
                                 @Qualifier(value = "consistentMenuStorage") MenuStorage menuStorage,
                                 HashtagParser hashtagParser,
                                 TopicStorage preferenceStorage) {
        this.api = api;
        this.menuStorage = menuStorage;
        this.hashtagParser = hashtagParser;
        this.preferenceStorage = preferenceStorage;
    }

    @Override
    public void handleMessage(GroupEventDAO<NewMessageWrapper> event) {
        final MessageDAO message = event.getObject().getMessage();
        final long peer = message.getFrom_id();
        final String text = message.getText().toLowerCase(Locale.ROOT);

        Optional<MenuItem> menuItem = menuStorage.getMenuByText(text);
        Optional<String> hashtag = Optional.empty();

        if (menuItem.isPresent()) {
            hashtag = hashtagParser
                    .parse(
                            Optional.ofNullable(
                                    menuItem.get().getResponseText()
                            )
                    );
        }


        if (hashtag.isPresent()) {
            handlePreference(hashtag.get(), peer, text);
        } else if (text.equals(ControlButton.HOME)) {
            sendMessage(menuStorage.getRoot(), peer);
        } else if (text.equals(ControlButton.BACK)) {
            MenuItem currentMenuItem = menuStorage
                    .getMenuById(
                            Long.parseLong(
                                    Optional.ofNullable(message.getPayload()).orElse("0")
                            )
                    )
                    .orElseThrow(NoSuchElementException::new);

            MenuItem parent = currentMenuItem.getParent();
            if (parent == null) {
                parent = menuStorage.getRoot();
            }
            sendMessage(parent, peer);
        } else {

            menuItem.ifPresent(item -> sendMessage(item, peer));
        }
    }

    private void handlePreference(final String hashtag, final long userId, final String text) {
        Optional<MenuItem> menuItem = menuStorage.getMenuByText(text);

        if (hashtag.equals(ServicePreferenceTag.ALL)) {
            preferenceStorage.addAllPreferenceToUser(userId);
            menuItem.ifPresent(item -> {
                sendMessage(
                        "Вы успешно подписались на все категории!",
                        makeKeyboard(item),
                        List.of(userId)
                );
            });
        } else if (hashtag.equals(ServicePreferenceTag.NONE)) {
            preferenceStorage.deleteAllPreferencesFromUser(userId);
            menuItem.ifPresent(item -> {
                sendMessage(
                        "Вы успешно отписались от всех категорий",
                        makeKeyboard(item),
                        List.of(userId)
                );
            });
        } else if (preferenceStorage.getByTag(hashtag).contains(userId)) {
            preferenceStorage.deletePreferenceFromUser(hashtag, userId);
            menuItem.ifPresent(item -> {
                sendMessage(
                        "Вы успешно отписались от категории " + item.getTrigger(),
                        makeKeyboard(item),
                        List.of(userId)
                );
            });

        } else {
            preferenceStorage.addPreferenceToUser(hashtag, userId);
            menuItem.ifPresent(item -> {
                sendMessage(
                        "Вы успешно подписались на категорию " + item.getTrigger(),
                        makeKeyboard(item),
                        List.of(userId)
                );
            });

        }

    }

    private void sendMessage(final String message, final KeyboardDAO keyboard, final List<Long> peers) {
        try {
            api.sendMessage(
                    new Message(
                            message,
                            keyboard
                    ),
                    peers
            );
        } catch (IOException | InterruptedException e) {
            log.error("Couldn't send message", e);
        }

    }

    private void sendMessage(final MenuItem menuItem, final Long peer) {
        final KeyboardDAO keyboard = makeKeyboard(menuItem);
        MenuAttachmentsDto attachments = menuItem.getAttachments();
        String text = attachments.getText();
        AtomicReference<String> topics = new AtomicReference<>();

        preferenceStorage.getTopicsByUser(peer)
                .stream()
                .map(menuStorage::getMenuByResponseText)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(MenuItem::getTrigger)
                .reduce((acc, cur) -> acc + ", " + cur)
                .ifPresentOrElse(
                        mergedTopics -> topics.set("Сейчас ты подписан на " + mergedTopics),
                        () -> topics.set("Пока ты ни на что не подписан(")
                );

//      Checking tag to print all user's subscriptions
        text = text.replace(
                ServicePreferenceTag.TOPICS,
                topics.get()
        );

        try {
            api.sendMessage(new Message(text, attachments.getAttachments(), keyboard), List.of(peer));
        } catch (IOException | InterruptedException e) {
            log.error("Couldn't send message", e);
        }
    }

    private KeyboardDAO makeKeyboard(final MenuItem item) {
        final KeyboardDAO keyboard = new KeyboardDAO();
        keyboard.setInline(false);
        keyboard.setOne_time(true);
        final List<Button[]> buttons = new ArrayList<>();

        for (MenuItem menuItem : item.getChildren()) {
            buttons.add(new Button[]{
                    new Button(
                            "secondary",
                            new TextAction(menuItem.getTrigger(), "")
                    )
            });
        }
        // Adding control buttons
        buttons.add(
                new Button[]{
                        new Button(
                                "primary",
                                new TextAction(ControlButton.BACK, String.valueOf(item.getId()))
                        ),
                        new Button(
                                "secondary",
                                new TextAction(ControlButton.HOME, "")
                        )
                }
        );

        keyboard.setButtons(buttons);
        return keyboard;
    }

    private static class ControlButton {
        private static final String HOME = "домой";
        private static final String BACK = "назад";
    }
}
