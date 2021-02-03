package com.jhoysbou.TBot.storage;

import java.util.List;
import java.util.Set;

public interface NewsPreferenceStorage {
    Set<Long> getByTag(final String tag);

    void addPreference(final String tag, final long userId);

    void addAllPreference(final long userId);

    void deletePreference(final String tag, final long userId);

    void deleteAllPreferences(final long userId);
}
