package exrf.pos.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginRequestDto {
    private String username;
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
