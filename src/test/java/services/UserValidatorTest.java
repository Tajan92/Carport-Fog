package services;
import app.dto.requestDTO.users.CreateCustomerDTO;
import app.services.utils.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidatorTest {
    CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("Morten", "Helander", "", "", "Morten123!", "42411031", "Carl Møllers Alle 43", "4769");

    String password = "Morten123!";
    String passwordNoNumber = "Morten";
    String shortPassword = "Morten1";
    String wrongEmail = "mortenh.hotmail,com";
    String wrongPassword = "mort";
    List<String> expectedMessage;


    @BeforeEach
    public void resetMessage() {
        expectedMessage = new ArrayList<>();
        UserValidator.getMessage().clear();
    }


    @Test
    public void passwordMustMatchTest() {
        expectedMessage.add("Adgangskoder skal være ens");
        UserValidator.passwordsMustMatch(password, wrongPassword);
        assertEquals(UserValidator.getMessage(), expectedMessage);
    }


    @Test
    public void validateEmailNotEmptyTest() {
        expectedMessage.add("Email skal udfyldes");
        UserValidator.validateEmailNotEmpty("");
        assertEquals(expectedMessage, UserValidator.getMessage());
    }


    @Test
    public void validatePasswordTest() {
        expectedMessage.add("Adgangskode skal udfyldes");
        UserValidator.validatePassword("");
        assertEquals(expectedMessage, UserValidator.getMessage());
    }


    @Test
    public void validateEmailTest() {
        expectedMessage.add("Email skal indeholde @ og .");
        UserValidator.validateEmail(wrongEmail);
        assertEquals(expectedMessage, UserValidator.getMessage());
    }


    @Test
    public void tooShortPasswordTest() {
        expectedMessage.add("Adgangskode er for kort");
        UserValidator.tooShortPassword(shortPassword);
        assertEquals(expectedMessage, UserValidator.getMessage());
    }


    @Test
    public void passwordMustContainNumber() {
        expectedMessage.add("Adgangskode skal indeholde tal");
        UserValidator.passwordMustContainNumber(passwordNoNumber);
        assertEquals(expectedMessage, UserValidator.getMessage());
    }


    @Test
    public void shouldRejectPasswordWithoutSpecialCharacterTest() {
        expectedMessage.add("Adgangskode skal indeholde mindst et specialtegn");
        UserValidator.shouldRejectPasswordWithoutSpecialCharacter(wrongPassword);
        assertEquals(expectedMessage, UserValidator.getMessage());
    }

    @Test
    public void validateTest() {
        expectedMessage.add("Email skal udfyldes");
        expectedMessage.add("Adgangskode skal udfyldes");
        expectedMessage.add("Adgangskode er for kort");
        expectedMessage.add("Adgangskode skal indeholde tal");
        expectedMessage.add("Adgangskode skal indeholde mindst et specialtegn");
        expectedMessage.add("Adgangskoder skal være ens");
        expectedMessage.add("Email skal indeholde @ og .");

        assertEquals(expectedMessage, UserValidator.validate(createCustomerDTO));
    }
}