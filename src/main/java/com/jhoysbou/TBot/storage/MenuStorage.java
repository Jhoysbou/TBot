package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.MenuItem;

import java.util.Optional;

public interface MenuStorage {
    MenuItem createMenuItem(final Optional<MenuItem> parent, final String trigger, final String responseText);

    MenuItem getRoot();

    void deleteMenuItem(final MenuItem item);

    MenuItem updateMenuItem(final MenuItem item, final String trigger, final String responseText);

    MenuItem updateMenuItemTrigger(final MenuItem item, final String trigger);

    MenuItem updateMenuItemResponse(final MenuItem item, final String responseText);

    Optional<MenuItem> getMenuByText(final String text);

    Optional<MenuItem> getMenuById(final long id);
}
