package app.dto.requestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginSalesRepDTO {
    private String email;
    private String password;
}
