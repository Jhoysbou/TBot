package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.Attachment;
import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.models.vkmodels.*;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.storage.MenuStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultFAQService implements FAQService {
    private final GroupApi api;
    private final MenuStorage menuStorage;

    @Autowired
    public DefaultFAQService(GroupApi api, MenuStorage menuStorage) {
        this.api = api;
        this.menuStorage = menuStorage;
    }

    @Override
    public void handleMessage(GroupEventDAO<NewMessageWrapper> event) {
        final MessageDAO message = event.getObject().getMessage();
        Optional<MenuItem> menuItem = menuStorage.getMenuByText(message.getText());

        if (menuItem.isPresent()) {


        }
    }

    @PostConstruct
    private void test() throws IOException, InterruptedException {
        final KeyboardDAO test = makeKeyboard();
        final List<Long> peers = new ArrayList<>();
        peers.add(137239419L);
        api.sendMessage(new Message("some text", test), peers);
    }

    private KeyboardDAO makeKeyboard() {
        final KeyboardDAO keyboard = new KeyboardDAO();
        keyboard.setInline(false);
        keyboard.setOne_time(true);
        final Button[][] buttons = new Button[2][2];

        buttons[0][0] = new Button("primary", new TextAction("Назад", ""));
        buttons[0][1] = new Button("primary", new TextAction("Домой", ""));
        buttons[1][0] = new Button("primary", new TextAction("Вариант1", ""));
        buttons[1][1] = new Button("primary", new TextAction("Варинт2", ""));
        keyboard.setButtons(buttons);
        return keyboard;
    }
}
