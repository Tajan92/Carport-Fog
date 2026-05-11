package app.entities;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class User {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String phoneNumber;
}
