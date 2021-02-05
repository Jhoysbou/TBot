package com.jhoysbou.TBot.utils.validation;

import com.jhoysbou.TBot.models.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemValidator implements Validator<MenuItem> {

    @Override
    public void validate(MenuItem menuItem) throws ValidationException {
        final String trigger = menuItem.getTrigger();
        if (trigger.length() > 40) {
            throw new ValidationException("This field cannot be longer than 40 characters");
        } else if (trigger.length() == 0) {
            throw new ValidationException("This field cannot be empty");
        } else if (menuItem.getChildren().size() > 9) {
            throw new ValidationException("You cannot create more than 9 elements");
        }
    }
}
