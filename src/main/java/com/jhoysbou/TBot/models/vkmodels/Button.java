package com.jhoysbou.TBot.models.vkmodels;

public class Button {
    private String color;
    private TextAction action;

    public Button(String color, TextAction action) {
        this.color = color;
        this.action = action;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TextAction getAction() {
        return action;
    }

    public void setAction(TextAction action) {
        this.action = action;
    }
}
