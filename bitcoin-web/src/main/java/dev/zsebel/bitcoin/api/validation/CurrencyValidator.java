package dev.zsebel.bitcoin.api.validation;

import java.util.Comparator;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import dev.zsebel.bitcoin.api.validation.annotation.SupportedCurrency;

public class CurrencyValidator implements ConstraintValidator<SupportedCurrency, String> {

    private final List<ValidationHandler> validationHandlers;
    private ValidationHandler validatorChain;

    @Autowired
    public CurrencyValidator(final List<ValidationHandler> validationHandlers) {
        this.validationHandlers = validationHandlers;
    }

    @Override
    public void initialize(final SupportedCurrency constraintAnnotation) {
        List<ValidationHandler> sortedValidators = sortValidators();
        this.validatorChain = buildChain(sortedValidators);
    }

    @Override
    public boolean isValid(final String currency, final ConstraintValidatorContext constraintValidatorContext) {
        return validatorChain.isValid(currency, constraintValidatorContext);
    }

    private List<ValidationHandler> sortValidators() {
        return validationHandlers.stream()
            .sorted(Comparator.comparingInt(this::getOrder))
            .toList();
    }

    private int getOrder(final ValidationHandler handler) {
        Order order = handler.getClass().getAnnotation(Order.class);
        return order != null ? order.value() : Integer.MAX_VALUE;
    }

    private ValidationHandler buildChain(final List<ValidationHandler> sortedValidators) {
        ValidationHandler head = sortedValidators.getFirst();
        ValidationHandler current = head;
        for (int i = 1; i < sortedValidators.size(); i++) {
            current = current.setNext(sortedValidators.get(i));
        }
        return head;
    }
}