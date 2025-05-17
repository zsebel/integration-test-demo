package com.epam.bitcoin.api.validation;

import jakarta.validation.ConstraintValidatorContext;

public interface ValidationHandler {

    ValidationHandler setNext(ValidationHandler handler);

    boolean isValid(String value, ConstraintValidatorContext context);
}
