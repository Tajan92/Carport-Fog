package app.dto.requestDTO.users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class CreateCustomerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordCheck;
    private String phoneNumber;
    private String address;
    private String zipCode;
}
