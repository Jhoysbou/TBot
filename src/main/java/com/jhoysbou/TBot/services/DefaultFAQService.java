package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.*;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storage.MenuStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class DefaultFAQService implements FAQService {
    private static final Logger log = LoggerFactory.getLogger(DefaultFAQService.class);

    private final GroupApi api;
    private final MenuStorage menuStorage;

    @Autowired
    public DefaultFAQService(GroupApi api, @Qualifier(value = "consistentMenuStorage") MenuStorage menuStorage) {
        this.api = api;
        this.menuStorage = menuStorage;
    }

    @Override
    public void handleMessage(GroupEventDAO<NewMessageWrapper> event) {
        final MessageDAO message = event.getObject().getMessage();
        final List<Long> peers = List.of(message.getFrom_id());
        final String text = message.getText().toLowerCase(Locale.ROOT);

        switch (text) {
            case ControlButton.HOME -> {
                sendMessage(menuStorage.getRoot(), peers);
            }
            case ControlButton.BACK -> {
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
                sendMessage(parent, peers);
            }
        }

        Optional<MenuItem> menuItem = menuStorage.getMenuByText(text);

        menuItem.ifPresent(item -> sendMessage(item, peers));
    }

    private void sendMessage(final MenuItem menuItem, final List<Long> peers) {
        final KeyboardDAO keyboard = makeKeyboard(menuItem);
        try {
            api.sendMessage(new Message(menuItem.getResponseText(), menuItem.getAttachments(), keyboard), peers);
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
