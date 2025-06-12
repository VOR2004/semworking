package ru.itis.semworkapp.controller.auth;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthMessages {
    public final String REG_FAILED_EMAIL = "Registration failed: email {} already exists";
    public final String REG_FAILED_PASSWORD = "Registration failed: password mismatch for email {}";
}
