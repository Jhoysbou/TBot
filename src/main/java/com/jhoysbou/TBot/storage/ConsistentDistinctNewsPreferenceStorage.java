package com.jhoysbou.TBot.storage;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ConsistentDistinctNewsPreferenceStorage implements NewsPreferenceStorage, Closeable {
    private static final Logger log = LoggerFactory.getLogger(ConsistentDistinctNewsPreferenceStorage.class);
    private static Map<String, Set<Long>> storage;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file;

    public ConsistentDistinctNewsPreferenceStorage(
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
    public Set<Long> getByTag(String tag) {
        return storage.get(tag);
    }

    @Override
    public void addPreference(String tag, long userId) {
        var users = getByTag(tag);
        users.add(userId);
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save newsPreference!", e);
        }
    }

    @Override
    public void addAllPreference(long userId) {
        storage.forEach((tag, users) -> users.add(userId));
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save newsPreference!", e);
        }
    }

    @Override
    public void deletePreference(String tag, long userId) {
        storage.get(tag).remove(userId);
        try {
            save();
        } catch (IOException e) {
            log.error("Couldn't save newsPreference!", e);
        }
    }

    @Override
    public void deleteAllPreferences(long userId) {
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
