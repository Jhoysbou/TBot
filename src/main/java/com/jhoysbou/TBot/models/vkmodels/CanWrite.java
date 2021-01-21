package com.jhoysbou.TBot.models.vkmodels;

public class CanWrite {
    private boolean allowed;
    private int reason;

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }
}
