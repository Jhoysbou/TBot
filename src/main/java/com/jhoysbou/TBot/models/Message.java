package com.jhoysbou.TBot.models;

import com.jhoysbou.TBot.models.vkmodels.KeyboardDAO;

public class Message {
    private String text;
    private Attachment attachment;
    private KeyboardDAO keyboard;

    public Message() {
    }

    public Message(String text, KeyboardDAO keyboard) {
        this.text = text;
        this.keyboard = keyboard;
    }

    public Message(String text, Attachment attachment, KeyboardDAO keyboard) {
        this.text = text;
        this.attachment = attachment;
        this.keyboard = keyboard;
    }

    public boolean hasAttachment() {
        return attachment != null;
    }

    public boolean hasKeyboard() {
        return keyboard != null;
    }

    public boolean hasText() {
        return text != null;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
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
