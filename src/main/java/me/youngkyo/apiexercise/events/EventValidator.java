package me.youngkyo.apiexercise.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice()) {
            errors.rejectValue("basePrice", "wrongValue", "Baseprice is wrong");
        }
    }
}
