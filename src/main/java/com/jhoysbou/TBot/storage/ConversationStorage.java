package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.vkmodels.ConversationDAO;
import com.jhoysbou.TBot.models.vkmodels.UserDAO;

import java.util.List;

public interface ConversationStorage {
    List<ConversationDAO> getAll();

    void addConversation(ConversationDAO conversationDAO);

    void addAllConversations(List<ConversationDAO> conversations);

    void deleteConversation(ConversationDAO conversationDAO);

}
