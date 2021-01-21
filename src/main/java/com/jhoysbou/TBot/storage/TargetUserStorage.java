package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.vkmodels.User;

import java.util.List;

public interface TargetUserStorage {

    List<User> getUsers();

    void addUsers(final List<User> users);

    void deleteUsers(final List<User> users);
}
