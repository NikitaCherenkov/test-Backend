package com.example.demo.validation;

import com.example.demo.repositiory.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistingCustomerValidator implements ConstraintValidator<ExistingCustomerCode, String> {

    private final CustomerRepository customerRepository;

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (code == null || code.trim().isEmpty()) {
            return true;
        }

        return customerRepository.existsByCode(code);
    }
}
