package com.jhoysbou.TBot.models.vkmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationWrapper {
    private long count;
    private List<Pair> items;
    private long unread_count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Pair> getItems() {
        return items;
    }

    public void setItems(List<Pair> items) {
        this.items = items;
    }

    public long getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(long unread_count) {
        this.unread_count = unread_count;
    }

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
}
