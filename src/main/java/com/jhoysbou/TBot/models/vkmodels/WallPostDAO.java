package com.jhoysbou.TBot.models.vkmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WallPostDAO {
    private long id;
    private long from_id;
    private long owner_id;
    private long date;
    private byte marked_as_ads;
    private String post_type;
    private String text;
    private long created_by;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFrom_id() {
        return from_id;
    }

    public void setFrom_id(long from_id) {
        this.from_id = from_id;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(long owner_id) {
        this.owner_id = owner_id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public byte getMarked_as_ads() {
        return marked_as_ads;
    }

    public void setMarked_as_ads(byte marked_as_ads) {
        this.marked_as_ads = marked_as_ads;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(long created_by) {
        this.created_by = created_by;
    }
}
