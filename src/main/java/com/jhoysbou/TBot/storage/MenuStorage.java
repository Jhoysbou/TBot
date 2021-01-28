package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.MenuItem;

import java.util.Optional;

public interface MenuStorage {
    MenuItem getRoot();

    void createMenuItem(final MenuItem parent, final MenuItem item);

    void deleteMenuItem(final MenuItem item);

    void updateMenuItem(final MenuItem item);

    Optional<MenuItem> getMenuByText(final String text);
}
