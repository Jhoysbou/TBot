package com.jhoysbou.TBot.storages;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MemoryDistinctUserIdStorage implements IdStorage<Long> {
    private static final Set<Long> storage = new HashSet<>();

    @Override
    public Set<Long> getAllIds() {
        return storage;
    }

    @Override
    public void addId(Long id) {
        storage.add(id);
    }

    @Override
    public void addAll(List<Long> ids) {
        storage.addAll(ids);
    }
}
