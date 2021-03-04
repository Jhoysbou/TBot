package com.jhoysbou.TBot.models.vkmodels.keyboard;


public class TextAction extends ButtonAction {
    private String type = "text";

    public TextAction(String label, String payload) {
        super(label, payload);
    }

    public String getType() {
        return type;
    }
}
