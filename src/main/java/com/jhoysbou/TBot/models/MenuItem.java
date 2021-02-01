package com.jhoysbou.TBot.models;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
    private long id;
    private List<MenuItem> children = new ArrayList<>();
    private MenuItem parent;
    private String trigger;
    private String responseText;

    public MenuItem() {

    }



    public MenuItem(long id, MenuItem parent, String trigger, String responseText) {
        this.id = id;
        this.parent = parent;
        this.trigger = trigger;
        this.responseText = responseText;
    }

    public MenuItem(MenuItem parent, String trigger) {
        this.parent = parent;
        this.trigger = trigger;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItem> children) {
        this.children = children;
    }

    public MenuItem getParent() {
        return parent;
    }

    public void setParent(MenuItem parent) {
        this.parent = parent;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}