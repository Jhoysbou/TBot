package com.jhoysbou.TBot.models;

import java.util.List;

public class MenuAttachmentsDto {
    private List<Attachment> attachments;
    private String text;

    public MenuAttachmentsDto() {
    }

    public MenuAttachmentsDto(List<Attachment> attachments, String text) {
        this.attachments = attachments;
        this.text = text;
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
}
