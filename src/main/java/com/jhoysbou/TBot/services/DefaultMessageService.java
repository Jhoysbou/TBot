package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.MenuAttachmentsDto;
import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.*;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storage.MenuStorage;
import com.jhoysbou.TBot.storage.NewsPreferenceStorage;
import com.jhoysbou.TBot.utils.HashtagParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class DefaultMessageService implements MessageService {
    private static final Logger log = LoggerFactory.getLogger(DefaultMessageService.class);

    private final GroupApi api;
    private final MenuStorage menuStorage;
    private final HashtagParser hashtagParser;
    private final NewsPreferenceStorage preferenceStorage;

    @Autowired
    public DefaultMessageService(GroupApi api,
                                 @Qualifier(value = "consistentMenuStorage") MenuStorage menuStorage,
                                 HashtagParser hashtagParser,
                                 NewsPreferenceStorage preferenceStorage) {
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
        final var payload = message.getPayload();
        var hashtag = hashtagParser.parse(Optional.ofNullable(payload));

        if (hashtag.isPresent()) {
            handlePreference(hashtag.get(), peer, text);
        } else if (text.equals(ControlButton.HOME)) {
            sendMessage(menuStorage.getRoot(), List.of(peer));
        } else if (text.equals(ControlButton.BACK)) {
            MenuItem currentMenuItem = menuStorage
                    .getMenuById(
                            Long.parseLong(
                                    Optional.ofNullable(payload).orElse("0")
                            )
                    )
                    .orElseThrow(NoSuchElementException::new);

            MenuItem parent = currentMenuItem.getParent();
            if (parent == null) {
                parent = menuStorage.getRoot();
            }
            sendMessage(parent, List.of(peer));
        } else {
            Optional<MenuItem> menuItem = menuStorage.getMenuByText(text);

            menuItem.ifPresent(item -> sendMessage(item, List.of(peer)));
        }
    }

    private void handlePreference(final String hashtag, final long userId, final String text) {
        Optional<MenuItem> menuItem = menuStorage.getMenuByText(text);

        if (hashtag.equals("#addall")) {
            preferenceStorage.addAllPreference(userId);
            menuItem.ifPresent(item -> {
                sendMessage(
                        "Вы успешно подписались на все категории!",
                        makeKeyboard(item),
                        List.of(userId)
                );
            });
        } else if (hashtag.equals("#delall")) {
            preferenceStorage.deleteAllPreferences(userId);
            menuItem.ifPresent(item -> {
                sendMessage(
                        "Вы успешно отписались от всех категорий",
                        makeKeyboard(item),
                        List.of(userId)
                );
            });
        } else if (preferenceStorage.getByTag(hashtag).contains(userId)) {
            preferenceStorage.deletePreference(hashtag, userId);
            menuItem.ifPresent(item -> {
                sendMessage(
                        "Вы успешно отписались от категории " + item.getTrigger(),
                        makeKeyboard(item),
                        List.of(userId)
                );
            });

        } else {
            preferenceStorage.addPreference(hashtag, userId);
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

    private void sendMessage(final MenuItem menuItem, final List<Long> peers) {
        final KeyboardDAO keyboard = makeKeyboard(menuItem);
        MenuAttachmentsDto attachments = menuItem.getAttachments();
        try {
            api.sendMessage(new Message(attachments.getText(), attachments.getAttachments(), keyboard), peers);
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
