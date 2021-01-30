package com.jhoysbou.TBot.models.vkmodels;

public class KeyboardDAO {
    private boolean one_time;
    private Button[][] buttons;
    private boolean inline;

    public boolean isOne_time() {
        return one_time;
    }

    public void setOne_time(boolean one_time) {
        this.one_time = one_time;
    }

    public Button[][] getButtons() {
        return buttons;
    }

    public void setButtons(Button[][] buttons) {
        this.buttons = buttons;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }
}
