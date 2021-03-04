package com.jhoysbou.TBot.models.vkmodels.keyboard;

import com.jhoysbou.TBot.models.vkmodels.keyboard.Button;

import java.util.List;

public class KeyboardDAO {
    private boolean one_time;
    private List<Button[]> buttons;
    private boolean inline;

    public boolean isOne_time() {
        return one_time;
    }

    public void setOne_time(boolean one_time) {
        this.one_time = one_time;
    }

    public List<Button[]> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button[]> buttons) {
        this.buttons = buttons;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }
}
