package dev.zsebel.bitcoin.api.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class InvalidCurrencyValidatorHandler extends AbstractCurrencyValidationHandler {

    private static final String ISO_THREE_LETTER_CURRENCY_REGEX_PATTERN = "^[A-Z]{3}$";
    private static final String VIOLATION_MESSAGE_TEMPLATE = "Invalid currency format [%s]. Only ISO 3-letter currency codes are supported.";

    @Override
    public boolean isValid(final String currency, final ConstraintValidatorContext constraintValidatorContext) {
        if (!currency.matches(ISO_THREE_LETTER_CURRENCY_REGEX_PATTERN)) {
            String violationMessage = String.format(VIOLATION_MESSAGE_TEMPLATE, currency);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                .buildConstraintViolationWithTemplate(violationMessage)
                .addConstraintViolation();
            return false;
        }
        return checkNext(currency, constraintValidatorContext);
    }
}
