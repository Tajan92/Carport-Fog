package app.exceptions;

public class CalculatorException extends Exception{
    public CalculatorException(String userMessage)
    {
        super(userMessage);
        System.out.println("userMessage: " + userMessage);
    }

    public CalculatorException(String userMessage, String systemMessage)
    {
        super(userMessage);
        System.out.println("userMessage: " + userMessage);
        System.out.println("errorMessage: " + systemMessage);
    }
}
