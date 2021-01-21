package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.vkmodels.UserDAO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemoryTargetUserStorage implements TargetUserStorage {
    private final static List<UserDAO> USER_DTO_STORAGE = new ArrayList<>();

    @Override
    public List<UserDAO> getUsers() {
        return USER_DTO_STORAGE;
    }

    @Override
    public void addUsers(List<UserDAO> userDAOS) {
        USER_DTO_STORAGE.addAll(userDAOS);
    }

    @Override
    public void deleteUsers(List<UserDAO> userDAOS) {
        USER_DTO_STORAGE.removeAll(userDAOS);
    }
}
