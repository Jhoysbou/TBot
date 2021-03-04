package com.jhoysbou.TBot.models.vkmodels.keyboard;

public class OpenLinkAction extends ButtonAction {
    private String type = "open_link";
    private String link;

    public OpenLinkAction(String label, String payload, String link) {
        super(label, payload);
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }
}
