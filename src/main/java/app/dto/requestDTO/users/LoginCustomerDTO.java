package app.dto.requestDTO.users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginCustomerDTO {
    private String email;
    private String password;
}
