package com.jhoysbou.TBot.models.vkmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewMessageWrapper {
    private MessageDAO message;
    private Object client_info;

    public MessageDAO getMessage() {
        return message;
    }

    public void setMessage(MessageDAO message) {
        this.message = message;
    }

    public Object getClient_info() {
        return client_info;
    }

    public void setClient_info(Object client_info) {
        this.client_info = client_info;
    }
}
