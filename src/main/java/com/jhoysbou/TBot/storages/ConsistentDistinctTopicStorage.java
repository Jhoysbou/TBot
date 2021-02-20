package com.jhoysbou.TBot.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Component
public class ConsistentDistinctTopicStorage implements TopicStorage, Closeable {
    private static final Logger log = LoggerFactory.getLogger(ConsistentDistinctTopicStorage.class);
    private static Map<String, Set<Long>> storage;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file;

    public ConsistentDistinctTopicStorage(
            @Value("${preference.storage.path}") String path) throws IOException {
        file = Paths.get(path).toFile();

        if (file.exists()) {
            storage = objectMapper
                    .readValue(file, new TypeReference<Map<String, Set<Long>>>() {});
        } else {
            storage = new HashMap<>();
        }
    }

    @Override
    public Optional<Set<Long>> getByTag(String tag) {
        return Optional.ofNullable(storage.get(tag));
    }

    @Override
    public List<String> getTopicsByUser(long userId) {
        List<String> topicsByUser = new ArrayList<>();
        storage.forEach((topic, users) -> {
            if (users.contains(userId)) {
                topicsByUser.add(topic);
            }
        });
        return topicsByUser;
    }

    @Override
    public void addNewPreference(String tag) {
        storage.put(tag, new HashSet<>());
    }

    @Override
    public void deletePreference(String tag) {
        storage.remove(tag);
    }

    @Override
    public void addPreferenceToUser(String tag, long userId) {
        var usersOpt = getByTag(tag);
        usersOpt.ifPresent(users -> {
            users.add(userId);
            try {
                save();
            } catch (IOException e) {
                log.error("Couldn't save newsPreference!", e);
            }
        });
    }

    @Override
    public void addAllPreferenceToUser(long userId) {
        storage.forEach((tag, users) -> users.add(userId));
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save newsPreference!", e);
        }
    }

    @Override
    public void deletePreferenceFromUser(String tag, long userId) {
        storage.get(tag).remove(userId);
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save newsPreference!", e);
        }
    }

    @Override
    public void deleteAllPreferencesFromUser(long userId) {
        storage.forEach((tag, users) -> users.remove(userId));
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save newsPreference!", e);
        }
    }

    private void save() throws IOException {
        objectMapper.writeValue(file, storage);

    }

    @Override
    @PreDestroy
    public void close() throws IOException {
        save();
    }
}
