package com.jhoysbou.TBot.models;

import com.jhoysbou.TBot.models.vkmodels.keyboard.KeyboardDAO;

import java.util.List;

public class Message {
    private String text;
    private List<Attachment> attachments;
    private KeyboardDAO keyboard;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public Message(String text, KeyboardDAO keyboard) {
        this.text = text;
        this.keyboard = keyboard;
    }

    public Message(String text, List<Attachment> attachments, KeyboardDAO keyboard) {
        this.text = text;
        this.attachments = attachments;
        this.keyboard = keyboard;
    }

    public boolean hasAttachment() {
        return attachments != null && attachments.size() != 0;
    }

    public boolean hasKeyboard() {
        return keyboard != null;
    }

    public boolean hasText() {
        return text != null;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public KeyboardDAO getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(KeyboardDAO keyboard) {
        this.keyboard = keyboard;
    }
}
