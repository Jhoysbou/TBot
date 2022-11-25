package com.jhoysbou.TBot.storages;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.utils.validation.ValidationException;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface MenuStorage {
  MenuItem createMenuItem(final Optional<MenuItem> parent,
      final String trigger,
      final String responseText) throws ValidationException;

  MenuItem getRoot();

  void deleteMenuItem(final MenuItem item);

  MenuItem updateMenuItem(final long id, final String trigger, final String responseText)
      throws NoSuchElementException, ValidationException;

  MenuItem updateMenuItemTrigger(final long id, final String trigger)
      throws NoSuchElementException, ValidationException;

  MenuItem updateMenuItemResponse(final long id, final String responseText)
      throws NoSuchElementException, ValidationException;

  Optional<MenuItem> getMenuByText(final String text);

  Optional<MenuItem> getMenuByResponseText(final String response);

  Optional<MenuItem> getMenuById(final long id);
}
