package app.dto.requestDTO.users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginSalesRepRequestDTO {
    private String email;
    private String password;
}
