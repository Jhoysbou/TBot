package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.MenuItem;

import java.util.ArrayList;
import java.util.Optional;

public class MemoryMenuStorage implements MenuStorage {
    private static final MenuItem root = new MenuItem(
            new ArrayList<MenuItem>(),
            null,
            ""
    );


    @Override
    public MenuItem getRoot() {
        return root;
    }

    @Override
    public void createMenuItem(MenuItem parent, MenuItem item) {
        parent.getChildren().add(item);
    }

    @Override
    public void deleteMenuItem(MenuItem item) {
        item.getParent().getChildren().remove(item);
    }

    @Override
    public void updateMenuItem(MenuItem item) {
        final MenuItem parent = item.getParent();
        deleteMenuItem(item);
        createMenuItem(parent, item);
    }

    @Override
    public Optional<MenuItem> getMenuByText(String text) {
        return find(root, text);
    }

    private Optional<MenuItem> find(final MenuItem currentItem, final String text) {
        if (currentItem.getText().equals(text)) {
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
