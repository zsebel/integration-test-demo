package com.epam.bitcoin.api.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class MissingCurrencyValidationHandler extends AbstractCurrencyValidationHandler {

    private static final String VIOLATION_MESSAGE = "Currency was not provided but it is mandatory for this API!";

    @Override
    public boolean isValid(final String currency, final ConstraintValidatorContext constraintValidatorContext) {
        if (currency == null || currency.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                .buildConstraintViolationWithTemplate(VIOLATION_MESSAGE)
                .addConstraintViolation();
        }
        return checkNext(currency, constraintValidatorContext);
    }
}
