package com.example.demo.dto.request;

import com.example.demo.enums.CustomerType;
import com.example.demo.validation.ExistingCustomerCode;
import com.example.demo.validation.NotSameFields;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotSameFields(
        message = "Main customer code points to the same customer",
        firstField = "code",
        secondField = "codeMainCustomer"
)
public class CustomerRequest {

    @NotBlank(message = "Code is required")
    private String customerCode;

    @NotBlank(message = "Name is required")
    private String customerName;

    @NotBlank(message = "INN is required")
    @Pattern(regexp = "^\\d{10}$|^\\d{12}$", message = "INN must be 10 or 12 digits")
    private String customerInn;

    @Pattern(regexp = "^\\d{9}$", message = "KPP must be 9 digits")
    private String customerKpp;

    private String customerLegalAddress;

    private String customerPostalAddress;

    @Email(message = "Invalid email")
    private String customerEmail;

    @ExistingCustomerCode
    private String customerCodeMain;

    @NotNull(message = "Customer type is required")
    private CustomerType customerType;
}