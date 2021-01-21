package com.jhoysbou.TBot.storage;

import com.jhoysbou.TBot.models.vkmodels.ConversationDAO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemoryConversationStorage implements ConversationStorage {
    private static final List<ConversationDAO> conversationStorage = new ArrayList<>();

    @Override
    public List<ConversationDAO> getAll() {
        return conversationStorage;
    }

    @Override
    public void addConversation(ConversationDAO conversationDAO) {
        conversationStorage.add(conversationDAO);
    }

    @Override
    public void addAllConversations(List<ConversationDAO> conversations) {
        conversationStorage.addAll(conversations);
    }

    @Override
    public void deleteConversation(ConversationDAO conversationDAO) {
        conversationStorage.remove(conversationDAO);
    }
}
