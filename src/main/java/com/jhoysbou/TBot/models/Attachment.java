package com.jhoysbou.TBot.models;

public class Attachment {
    private String type;
    private String owner_id;
    private String media_id;

    public Attachment() {
    }

    public Attachment(String type, String owner_id, String media_id) {
        this.type = type;
        this.owner_id = owner_id;
        this.media_id = media_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }
}
