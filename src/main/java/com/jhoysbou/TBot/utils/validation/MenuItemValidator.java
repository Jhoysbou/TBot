package com.jhoysbou.TBot.utils.validation;

import com.jhoysbou.TBot.models.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemValidator implements Validator<MenuItem> {

    @Override
    public void validate(MenuItem menuItem) throws ValidationException {
        final String trigger = menuItem.getTrigger();
        final int triggerLength = trigger.length();
        final int responseLength = menuItem.getResponseText().length();
        if (triggerLength > 40) {
            throw new ValidationException("This field cannot be longer than 40 characters. Current length "
                    + triggerLength);
        } else if (triggerLength == 0) {
            throw new ValidationException("Button text cannot be empty");
        } else if (menuItem.getParent() != null && menuItem.getChildren().size() > 5) {
            throw new ValidationException("You cannot create more than 5 elements");
        } else if (menuItem.getParent() == null && menuItem.getChildren().size() > 6) {
            throw new ValidationException("You cannot create more than 6 elements");
        } else if (responseLength > 4096) {
            throw new ValidationException("Response message cannot be greater than 4096 characters. Current length "
                    + responseLength);
        }
    }
}
