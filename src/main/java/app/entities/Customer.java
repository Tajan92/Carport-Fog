package app.entities;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)

public class Customer extends User {
    private String address;
    private String zipCode;
    private String town;

    public Customer(int id, String firstName, String lastName, String email, String password, String phoneNumber, String address, String zipCode, String town) {
        super(id, firstName, lastName, email, password, phoneNumber);
        this.address = address;
        this.zipCode = zipCode;
        this.town = town;
    }

    public Customer(String firstName, String lastName, String email, String password, String phoneNumber, String address, String zipCode, String town) {
        super(firstName, lastName, email, password, phoneNumber);
        this.address = address;
        this.zipCode = zipCode;
        this.town = town;
    }
}