package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.vkmodels.ConversationDAO;
import com.jhoysbou.TBot.models.vkmodels.UserDAO;

import java.util.List;

public interface IdStorage {
    List<Long> getAll();

    void addId(Long id);

    void addAllIds(List<Long> ids);

    void deleteId(Long id);

}
