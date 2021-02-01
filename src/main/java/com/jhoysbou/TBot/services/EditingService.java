package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.MenuItem;

import java.util.Optional;

public interface EditingService {
    MenuItem getRoot();

    MenuItem getMenuItemById(final long id);

    void updateMenuItem(final long id,
                        final Optional<String> trigger,
                        final Optional<String> responseText);

    void createNewMenuItem(final long parentId);

    void deleteMenuItem(final long id);
}
