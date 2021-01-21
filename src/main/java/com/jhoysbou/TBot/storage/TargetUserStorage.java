package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.vkmodels.UserDAO;

import java.util.List;

public interface TargetUserStorage {

    List<UserDAO> getUsers();

    void addUsers(final List<UserDAO> userDAOS);

    void deleteUsers(final List<UserDAO> userDAOS);
}
