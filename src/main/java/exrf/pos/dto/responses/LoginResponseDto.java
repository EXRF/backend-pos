package exrf.pos.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private String username;
    private String email;
    private List<String> roles;

}
