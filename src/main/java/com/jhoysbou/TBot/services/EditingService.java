package com.jhoysbou.TBot.services;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.utils.validation.ValidationException;

import java.util.Optional;

public interface EditingService {
  MenuItem getRoot();

  MenuItem getMenuItemById(final long id);

  void updateMenuItem(final long id,
      final Optional<String> trigger,
      final Optional<String> responseText,
      final Optional<Boolean> isSubscriptionRequred) throws ValidationException;

  void createNewMenuItem(final long parentId) throws ValidationException;

  void deleteMenuItem(final long id);
}
