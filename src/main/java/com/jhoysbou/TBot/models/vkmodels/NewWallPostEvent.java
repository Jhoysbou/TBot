package com.jhoysbou.TBot.models.vkmodels;

public class NewWallPostEvent {
    private String event_id;
    private String type;
    private String group_id;
    private WallPost object;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public WallPost getObject() {
        return object;
    }

    public void setObject(WallPost object) {
        this.object = object;
    }
}
