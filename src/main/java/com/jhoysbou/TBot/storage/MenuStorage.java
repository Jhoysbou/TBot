package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.MenuItem;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface MenuStorage {
    MenuItem createMenuItem(final Optional<MenuItem> parent, final String trigger, final String responseText);

    MenuItem getRoot();

    void deleteMenuItem(final MenuItem item);

    MenuItem updateMenuItem(final long id, final String trigger, final String responseText) throws NoSuchElementException;

    MenuItem updateMenuItemTrigger(final long id, final String trigger) throws NoSuchElementException;

    MenuItem updateMenuItemResponse(final long id, final String responseText) throws NoSuchElementException;

    Optional<MenuItem> getMenuByText(final String text);

    Optional<MenuItem> getMenuById(final long id);
}