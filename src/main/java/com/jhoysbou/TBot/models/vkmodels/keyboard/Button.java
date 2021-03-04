package com.jhoysbou.TBot.models.vkmodels.keyboard;

public class Button {
    private ButtonAction action;

    public Button(ButtonAction action) {
        this.action = action;
    }

    public ButtonAction getAction() {
        return action;
    }

    public void setAction(ButtonAction action) {
        this.action = action;
    }
}
