package app.dto.requestDTO.users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class CustomerRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordCheck;
    private String phoneNumber;
    private String address;
    private String zipCode;
}
