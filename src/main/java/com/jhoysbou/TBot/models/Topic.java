package com.jhoysbou.TBot.models;

public class Topic {
    private String hashtag;
    private String name;

    public Topic() {
    }

    public Topic(String hashtag, String name) {
        this.hashtag = hashtag;
        this.name = name;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
