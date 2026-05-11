package app.entities;
import lombok.Getter;

@Getter

public class Customer extends User {
    private String address;
    private String zipCode;
    private String city;

    public Customer(int customerId, String firstName, String lastName, String email, String password, String phoneNumber, String address, String zipCode, String city) {
        super(customerId, firstName, lastName, email, password, phoneNumber);
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
    }
}