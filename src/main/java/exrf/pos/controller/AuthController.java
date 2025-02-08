package exrf.pos.controller;

import exrf.pos.dto.requests.LoginRequestDto;
import exrf.pos.dto.requests.RefreshTokenRequestDto;
import exrf.pos.dto.requests.SignupRequestDto;
import exrf.pos.dto.responses.JwtRefreshResponseDto;
import exrf.pos.dto.responses.LoginResponseDto;
import exrf.pos.service.AuthService;
import exrf.pos.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto loginResponseDto = authService.login(loginRequest);

        return ResponseEntity.ok()
                .body(ResponseUtil.responseSuccess(LoginResponseDto.class, loginResponseDto, "Login successful"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signUpRequest) {
        authService.signup(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.responseSuccess(AuthController.class, "User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok(ResponseUtil.responseSuccess(AuthController.class, "User logout successfully"));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDto requestDto) {
        JwtRefreshResponseDto responseDto = authService.refreshToken(requestDto);
        return ResponseEntity.ok(ResponseUtil.responseSuccess(JwtRefreshResponseDto.class, responseDto, "Success"));
    }
}