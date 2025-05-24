package dev.zsebel.bitcoin.api.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import dev.zsebel.bitcoin.api.validation.CurrencyValidator;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
public @interface SupportedCurrency {
    String message() default "An expected error occurred during Currency validation.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
