package com.epam.bitcoin.api.validation;

import jakarta.validation.ConstraintValidatorContext;

public abstract class AbstractCurrencyValidationHandler implements ValidationHandler {

    private ValidationHandler nextHandler;

    @Override
    public ValidationHandler setNext(final ValidationHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    protected boolean checkNext(final String currency, final ConstraintValidatorContext context) {
        return nextHandler == null || nextHandler.isValid(currency, context);
    }
}
