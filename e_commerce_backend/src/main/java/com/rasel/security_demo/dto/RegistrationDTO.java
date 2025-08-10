package com.rasel.security_demo.dto;

import com.rasel.security_demo.model.User;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegistrationDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private User.Gender gender;

    @Past
    private LocalDate birthDate;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$")
    private String phoneNumber;

    @NotBlank
    @Size(min = 8)
    private String password;

    private String doorNo;
    private String street;
    private String city;
    private String district;
    private String state;
    private String role;
}
