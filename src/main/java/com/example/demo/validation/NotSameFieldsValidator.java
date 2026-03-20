package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class NotSameFieldsValidator implements ConstraintValidator<NotSameFields, Object> {

    private String field;
    private String fieldMatch;
    private String message;

    @Override
    public void initialize(NotSameFields annotation) {
        this.field = annotation.firstField();
        this.fieldMatch = annotation.secondField();
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
            Object fieldValue = wrapper.getPropertyValue(field);
            Object fieldMatchValue = wrapper.getPropertyValue(fieldMatch);

            if (fieldValue == null || fieldMatchValue == null) {
                return true;
            }

            boolean isValid = !fieldValue.toString().equalsIgnoreCase(fieldMatchValue.toString());

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(field)
                        .addConstraintViolation();
            }

            return isValid;

        } catch (Exception e) {
            return true;
        }
    }
}