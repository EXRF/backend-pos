package exrf.pos.controller;

import exrf.pos.dto.requests.LoginRequestDto;
import exrf.pos.dto.requests.SignupRequestDto;
import exrf.pos.dto.responses.MessageResponseDto;
import exrf.pos.dto.responses.UserInfoResponseDto;
import exrf.pos.model.enums.ERole;
import exrf.pos.model.Role;
import exrf.pos.model.User;
import exrf.pos.repository.RoleRepository;
import exrf.pos.repository.UserRepository;
import exrf.pos.security.JwtUtils;
import exrf.pos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserService userDetails = (UserService) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponseDto(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponseDto("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponseDto("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> requestRole = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        for (String role : requestRole) {
            switch (role.toLowerCase()) {
                case "admin":
                    Role adminRole = roleRepository.findByRole(ERole.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Admin role not found."));
                    roles.add(adminRole);
                    break;
                case "cashier":
                    Role cashierRole = roleRepository.findByRole(ERole.CASHIER)
                            .orElseThrow(() -> new RuntimeException("Error: Cashier role not found."));
                    roles.add(cashierRole);
                    break;
                default:
                    return ResponseEntity.badRequest()
                            .body(new MessageResponseDto("Error: Invalid role specified."));
            }
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponseDto("You've been signed out!"));
    }
}