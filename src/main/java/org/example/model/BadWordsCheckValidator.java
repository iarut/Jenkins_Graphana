package org.example.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class BadWordsCheckValidator implements ConstraintValidator<BadWordsCheck, String> {
    List<String> badwords = Arrays.asList("badword1", "badword2", "badword3");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !badwords.contains(value);
    }
}
