package com.jhoysbou.TBot.models;

import java.util.List;

public class MenuItem {
    private List<MenuItem> children;
    private MenuItem parent;
    private String text;

    public MenuItem() {

    }

    public MenuItem(List<MenuItem> children, MenuItem parent, String text) {
        this.children = children;
        this.parent = parent;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
