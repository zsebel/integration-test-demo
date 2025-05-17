package dev.zsebel.bitcoin.api.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Component
@Order(3)
public class SupportedCurrencyValidatorHandler extends AbstractCurrencyValidationHandler {

    private static final String VIOLATION_MESSAGE_TEMPLATE = "Invalid currency [%s]. Supported currencies: %s";
    private final Set<String> supportedCurrencies;

    @Autowired
    public SupportedCurrencyValidatorHandler(final Map<String, Locale> supportedLocalesByCurrency) {
        this.supportedCurrencies = supportedLocalesByCurrency.keySet();
    }

    @Override
    public boolean isValid(final String currency, final ConstraintValidatorContext constraintValidatorContext) {
        if (!supportedCurrencies.contains(currency)) {
            String violationMessage = String.format(VIOLATION_MESSAGE_TEMPLATE, currency, supportedCurrencies);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                .buildConstraintViolationWithTemplate(violationMessage)
                .addConstraintViolation();
            return false;
        }
        return checkNext(currency, constraintValidatorContext);
    }
}
