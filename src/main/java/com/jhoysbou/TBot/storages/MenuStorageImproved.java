package com.jhoysbou.TBot.storages;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.utils.validation.ValidationException;

public interface MenuStorageImproved extends MenuStorage {
  MenuItem updateMenuItem(final long id,
      final Optional<String> trigger,
      final Optional<String> responseText,
      final Optional<Boolean> isSubscriptionRequired)
      throws NoSuchElementException, ValidationException;

  MenuItem createMenuItem(final Optional<MenuItem> parent,
      final String trigger,
      final String responseText,
      final Boolean isSubscriptionRequired) throws ValidationException;
}
