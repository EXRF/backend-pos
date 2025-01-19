package exrf.pos.controller;

import exrf.pos.dto.requests.LoginRequestDto;
import exrf.pos.dto.requests.RefreshTokenRequestDto;
import exrf.pos.dto.requests.SignupRequestDto;
import exrf.pos.dto.responses.JwtRefreshResponseDto;
import exrf.pos.dto.responses.LoginResponseDto;
import exrf.pos.exception.InvalidRoleException;
import exrf.pos.exception.RefreshTokenException;
import exrf.pos.model.RefreshToken;
import exrf.pos.model.enums.ERole;
import exrf.pos.model.Role;
import exrf.pos.model.User;
import exrf.pos.repository.RoleRepository;
import exrf.pos.repository.UserRepository;
import exrf.pos.security.JwtUtils;
import exrf.pos.service.RefreshTokenService;
import exrf.pos.service.UserDetailsImpl;
import exrf.pos.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        User user = userOptional.get();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        user.setLastLoginAt(LocalDateTime.now());
        user.setIsLogin(true);
        userRepository.save(user);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwtToken(authentication, roles);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        LoginResponseDto loginResponseDto = new LoginResponseDto(jwt, refreshToken.getToken(), refreshToken.getExpiryDate());

        return ResponseEntity.ok()
                .body(ResponseUtil.responseSuccess(LoginResponseDto.class, loginResponseDto, "Login successful"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(ResponseUtil.responseError(AuthController.class, "Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(ResponseUtil.responseError(AuthController.class, "Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));


        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        Set<String> validRoles = Set.of("admin", "cashier");

        if (strRoles != null && !validRoles.containsAll(strRoles)) {
            throw new InvalidRoleException("Invalid roles provided. Allowed roles are: " + validRoles);
        }

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByRole(ERole.ROLE_CASHIER)
                    .orElseThrow(() -> new InvalidRoleException("Error: Default role 'cashier' not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                ERole eRole = role.equals("admin") ? ERole.ROLE_ADMIN : ERole.ROLE_CASHIER;
                Role mappedRole = roleRepository.findByRole(eRole)
                        .orElseThrow(() -> new InvalidRoleException("Error: Role '" + role + "' not found."));
                roles.add(mappedRole);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.responseSuccess(AuthController.class, "User registered successfully!"));
    }

    @PostMapping("signout")
    public ResponseEntity<?> signOutUser(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(ResponseUtil.responseError(AuthController.class, "Invalid or missing token"));
        }

        String jwt = bearerToken.substring(7);

        if (!jwtUtils.validateJwtToken(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.responseError(AuthController.class, "Invalid token"));
        }

        String username = jwtUtils.getUsernameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found"));

        user.setIsLogin(false);
        userRepository.save(user);

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ResponseUtil.responseSuccess(AuthController.class, "User signout successfully"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDto dto) {
        String refreshToken = dto.getRefreshToken();

        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RefreshTokenException("Refresh Token is not in database or has expired!"));

        User user = token.getUser();
        List<String> roles = user.getRoles().stream().map(role -> role.getRole().name()).collect(Collectors.toList());

        String jwtToken = jwtUtils.generateJwtToken(user.getUsername(), roles);

        JwtRefreshResponseDto responseDto = new JwtRefreshResponseDto(jwtToken, refreshToken);

        return ResponseEntity.ok(ResponseUtil.responseSuccess(JwtRefreshResponseDto.class, responseDto, "Success"));
    }
}