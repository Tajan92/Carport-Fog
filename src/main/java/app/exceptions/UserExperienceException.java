package app.exceptions;

public class UserExperienceException extends Exception {
    public UserExperienceException(String userMessage)
    {
        super(userMessage);
        System.out.println("userMessage: " + userMessage);
    }
}
