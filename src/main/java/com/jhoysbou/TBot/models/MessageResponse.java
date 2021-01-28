package com.jhoysbou.TBot.models;

public class MessageResponse {
    private String text;
    private Attachment attachment;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
