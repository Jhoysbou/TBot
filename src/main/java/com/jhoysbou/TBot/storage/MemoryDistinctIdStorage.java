package com.jhoysbou.TBot.storage;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemoryDistinctIdStorage implements IdStorage {
    private static final List<Long> idStorage = new ArrayList<>();

    @Override
    public List<Long> getAll() {
        return idStorage;
    }

    @Override
    public void addId(Long id) {
        if (!idStorage.contains(id)) {
            idStorage.add(id);
        }
    }

    @Override
    public void addAllIds(List<Long> ids) {
        if (!idStorage.containsAll(ids)) {
            idStorage.addAll(ids);
        }
    }

    @Override
    public void deleteId(Long id) {
        idStorage.remove(id);
    }
}
