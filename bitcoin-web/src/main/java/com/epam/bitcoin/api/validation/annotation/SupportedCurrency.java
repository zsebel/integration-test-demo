package com.epam.bitcoin.api.validation.annotation;

import com.epam.bitcoin.api.validation.CurrencyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
public @interface SupportedCurrency {
    String message() default "An expected error occurred during Currency validation.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
