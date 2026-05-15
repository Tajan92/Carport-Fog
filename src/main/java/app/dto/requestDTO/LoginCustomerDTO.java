package app.dto.requestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginCustomerDTO {
    private String email;
    private String password;
}
