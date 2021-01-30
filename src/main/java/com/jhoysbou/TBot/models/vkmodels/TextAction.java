package com.jhoysbou.TBot.models.vkmodels;

public class TextAction {
    private String type = "text";
    private String label;
    private String payload;

    public TextAction(String label, String payload) {
        this.label = label;
        this.payload = payload;
    }

    public String getType() {
        return type;
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
