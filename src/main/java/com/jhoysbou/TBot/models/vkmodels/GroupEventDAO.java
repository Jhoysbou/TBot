package com.jhoysbou.TBot.models.vkmodels;

public class GroupEventDAO<T> {
    private String event_id;
    private String type;
    private String v;
    private String group_id;
    private T object;

    public String getV() {
    	return v;
    }

    public void setV(String v) {
    	this.v = v;
    }

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

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
