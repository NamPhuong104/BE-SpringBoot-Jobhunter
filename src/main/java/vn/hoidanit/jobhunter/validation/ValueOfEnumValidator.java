package vn.hoidanit.jobhunter.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    private String[] acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        Class<? extends Enum<?>> enumClass = annotation.enumClass();
        this.acceptedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false; // treat null as invalid; use @Nullable if you want allow null
        for (String v : acceptedValues) {
            if (v.equalsIgnoreCase(value.trim())) return true;
        }
        return false;
    }
}
