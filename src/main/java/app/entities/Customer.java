package app.entities;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)

public class Customer extends User {
    private String address;
    private String zipCode;
    private String city;

    public Customer(int id, String firstName, String lastName, String email, String password, String phoneNumber, String address, String zipCode, String city) {
        super(id, firstName, lastName, email, password, phoneNumber);
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
    }

    public Customer(String firstName, String lastName, String email, String password, String phoneNumber, String address, String zipCode, String city) {
        super(firstName, lastName, email, password, phoneNumber);
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
    }
}