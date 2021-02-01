package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.storage.MenuStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DefaultEditingService implements EditingService {
    private final MenuStorage storage;

    @Autowired
    public DefaultEditingService(MenuStorage storage) {
        this.storage = storage;
    }

    @Override
    public MenuItem getRoot() {
        return storage.getRoot();
    }

    @Override
    public MenuItem getMenuItemById(long id) {
        return storage.getMenuById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void updateMenuItem(long id, Optional<String> trigger, Optional<String> responseText) {
        if (trigger.isPresent() && responseText.isPresent()) {
            storage.updateMenuItem(id, trigger.get(), responseText.get());
        } else {
            trigger.ifPresent(s -> storage.updateMenuItemTrigger(id, s));
            responseText.ifPresent(s -> storage.updateMenuItemResponse(id, s));
        }
    }
}
