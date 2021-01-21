package com.jhoysbou.TBot.models.vkmodels;

public class ResponseWrapper<T> {
    private T response;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
