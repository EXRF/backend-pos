package exrf.pos.dto.responses;

import exrf.pos.model.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
//    private String username;
//    private String email;
//    private List<String> roles;
//    private TokenDto token;
    private String token;
    private String refreshToken;
    private Instant expiryDate;

//    @Setter
//    @Getter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class TokenDto {
//        private String token;
//        private String refreshToken;
//        private Instant expiryDate;
//
//    }

}
