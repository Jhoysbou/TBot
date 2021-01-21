package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.vkmodels.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemoryTargetUserStorage implements TargetUserStorage {
    private final static List<User> userStorage = new ArrayList<>();

    @Override
    public List<User> getUsers() {
        return userStorage;
    }

    @Override
    public void addUsers(List<User> users) {
        userStorage.addAll(users);
    }

    @Override
    public void deleteUsers(List<User> users) {
        userStorage.removeAll(users);
    }
}
