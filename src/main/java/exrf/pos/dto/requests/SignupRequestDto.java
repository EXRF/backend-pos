package exrf.pos.dto.requests;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    private Set<String> role;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,40}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

}
