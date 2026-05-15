package app.dto.requestDTO.users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginSalesRepDTO {
    private String email;
    private String password;
}
