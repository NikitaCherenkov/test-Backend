package com.example.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotSameFieldsValidator.class)
@Documented
public @interface NotSameFields {

    String message() default "Field values must be different";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String firstField() default "code";
    String secondField() default "codeMainCustomer";
}