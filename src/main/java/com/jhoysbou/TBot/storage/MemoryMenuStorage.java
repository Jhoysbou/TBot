package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.MenuItem;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
public class MemoryMenuStorage implements MenuStorage {
    private static long COUNTER = 0;
    private static final MenuItem root = new MenuItem(
            COUNTER++,
            null,
            "start",
            "Привет!"
    );


    @Override
    public MenuItem createMenuItem(Optional<MenuItem> parent, String trigger, String responseText) {
        MenuItem parentItem = root;
        if (parent.isPresent()) {
            parentItem = parent.get();
        }

        final MenuItem item = new MenuItem(COUNTER++, parentItem, trigger, responseText);
        parentItem.getChildren().add(item);
        return item;
    }

    @Override
    public MenuItem getRoot() {
        return root;
    }

    @Override
    public void deleteMenuItem(MenuItem item) {
        item.getParent().getChildren().remove(item);
    }

    @Override
    public MenuItem updateMenuItem(MenuItem item, String trigger, String responseText) {
        item.setTrigger(trigger);
        item.setResponseText(responseText);
        return item;
    }

    @Override
    public MenuItem updateMenuItemTrigger(MenuItem item, String trigger) {
        item.setTrigger(trigger);
        return item;
    }

    @Override
    public MenuItem updateMenuItemResponse(MenuItem item, String responseText) {
        item.setResponseText(responseText);
        return item;
    }


    @Override
    public Optional<MenuItem> getMenuByText(String text) {
        return find(root, text);
    }

    @Override
    public Optional<MenuItem> getMenuById(long id) {
        return find(root, id);
    }


    private Optional<MenuItem> find(final MenuItem currentItem, final long id) {
        if (currentItem.getId() == id) {
            return Optional.of(currentItem);
        }

        for (MenuItem item : currentItem.getChildren()) {
            Optional<MenuItem> result = find(item, id);
            if (result.isPresent()) {
                return result;
            }
        }


        return Optional.empty();
    }

    // Case insensitive
    private Optional<MenuItem> find(final MenuItem currentItem, final String text) {
        if (currentItem.getTrigger().toLowerCase(Locale.ROOT).equals(text.toLowerCase(Locale.ROOT))) {
            return Optional.of(currentItem);
        }

        for (MenuItem item : currentItem.getChildren()) {
            Optional<MenuItem> result = find(item, text);
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }
}
