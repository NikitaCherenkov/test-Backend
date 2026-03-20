package com.example.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingCustomerValidator.class)
@Documented
public @interface ExistingCustomerCode {

    String message() default "Customer with this code does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
