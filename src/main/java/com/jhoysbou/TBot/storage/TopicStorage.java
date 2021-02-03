package com.jhoysbou.TBot.storage;

import java.util.List;
import java.util.Set;

public interface TopicStorage {
    Set<Long> getByTag(final String tag);

    List<String> getTopicsByUser(final long userId);

    void addNewPreference(final String tag);

    void deletePreference(final String tag);

    void addPreferenceToUser(final String tag, final long userId);

    void addAllPreferenceToUser(final long userId);

    void deletePreferenceFromUser(final String tag, final long userId);

    void deleteAllPreferencesFromUser(final long userId);
}
