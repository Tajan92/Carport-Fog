package app.services.utils;

import app.dto.requestDTO.users.CustomerRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {
    static List<String> message = new ArrayList<>();

    public static List<String> validate(CustomerRequestDTO customerRequestDTO) {
        String email = customerRequestDTO.getEmail();
        String password = customerRequestDTO.getPassword();
        String passwordCheck = customerRequestDTO.getPasswordCheck();

        validateEmailNotEmpty(email);
        validatePassword(password);
        tooShortPassword(password);
        passwordMustContainNumber(password);
        shouldRejectPasswordWithoutSpecialCharacter(password);
        passwordsMustMatch(password, passwordCheck);
        validateEmail(email);
        return message;
    }

    public static void passwordsMustMatch(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            message.add("Adgangskoder skal være ens");
        }
    }

    public static void validateEmailNotEmpty(String email) {
        if (email.isBlank()) {
            message.add("Email skal udfyldes");
        }
    }

    public static void validatePassword(String password) {
        if (password.isBlank() || password == null) {
            message.add("Adgangskode skal udfyldes");
        }
    }

    public static void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            message.add("Email skal indeholde @ og .");
        }
    }

    public static void tooShortPassword(String password) {
        if (password.length() < 8) {
            message.add("Adgangskode er for kort");
        }
    }

    public static void passwordMustContainNumber(String password) {
        if (!password.chars().anyMatch(Character::isDigit)) {
            message.add("Adgangskode skal indeholde tal");
        }
    }

    public static void shouldRejectPasswordWithoutSpecialCharacter(String password) {
        if (!password.chars().anyMatch(c -> !Character.isLetterOrDigit(c))) {
            message.add("Adgangskode skal indeholde mindst et specialtegn");
        }
    }

    public static List<String> getMessage() {
        return message;
    }
}
