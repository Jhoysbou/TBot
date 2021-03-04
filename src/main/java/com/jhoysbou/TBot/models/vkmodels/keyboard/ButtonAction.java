package com.jhoysbou.TBot.models.vkmodels.keyboard;

public abstract class ButtonAction {
    private String label;
    private String payload;

    public ButtonAction(String label, String payload) {
        this.label = label;
        this.payload = payload;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
