package com.jhoysbou.TBot.models.vkmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pair {
    private ConversationDAO conversation;
    private MessageDAO last_message;

    public ConversationDAO getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDAO conversation) {
        this.conversation = conversation;
    }

    public MessageDAO getLast_message() {
        return last_message;
    }

    public void setLast_message(MessageDAO last_message) {
        this.last_message = last_message;
    }
}
