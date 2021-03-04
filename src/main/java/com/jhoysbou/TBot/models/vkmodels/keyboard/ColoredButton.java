package com.jhoysbou.TBot.models.vkmodels.keyboard;

public class ColoredButton extends Button {
    private String color;

    public ColoredButton(String color, ButtonAction action) {
        super(action);
        this.color = color;
    }
}
