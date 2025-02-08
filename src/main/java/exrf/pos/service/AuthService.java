package exrf.pos.service;

import exrf.pos.dto.requests.LoginRequestDto;
import exrf.pos.dto.requests.RefreshTokenRequestDto;
import exrf.pos.dto.requests.SignupRequestDto;
import exrf.pos.dto.responses.JwtRefreshResponseDto;
import exrf.pos.dto.responses.LoginResponseDto;
import exrf.pos.exception.*;
import exrf.pos.model.RefreshToken;
import exrf.pos.model.Role;
import exrf.pos.model.User;
import exrf.pos.repository.RoleRepository;
import exrf.pos.repository.UserRepository;
import exrf.pos.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    public void signup(SignupRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateResourceException("Username is already exists");
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Email is already exists");
        }

        String roleName = (requestDto.getRole() == null || requestDto.getRole().isEmpty()) ? "USER" : requestDto.getRole().toUpperCase();

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new InvalidRoleException("Role not Found"));

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(role);

        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Optional<User> optionalUser;
        if (requestDto.getEmail() != null && !requestDto.getEmail().isEmpty()) {
            optionalUser = userRepository.findByEmail(requestDto.getEmail());
        } else if (requestDto.getUsername() != null && !requestDto.getUsername().isEmpty()) {
            optionalUser = userRepository.findByUsername(requestDto.getUsername());
        } else {
            throw new InvalidRequestException("Username or Email is required");
        }

        User user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), requestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        user.setIsLogin(true);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String jwt = jwtUtils.generateJwtToken(authentication, user.getRole().getName());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return new LoginResponseDto(
                jwt,
                refreshToken.getToken(),
                refreshToken.getExpiryDate()
        );
    }

    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new RuntimeException("User is not authenticated");

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//        Throw if isLogin true to avoid fraud for multiple request
        if (!user.getIsLogin())
            throw new RuntimeException("User already logout, please make a new sign in");

        user.setIsLogin(false);
        userRepository.save(user);
        refreshTokenService.deleteByUserId(user.getId());
        SecurityContextHolder.clearContext();
    }

    public JwtRefreshResponseDto refreshToken(RefreshTokenRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new RuntimeException("User is not authenticated");

        String refreshToken = requestDto.getRefreshToken();
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RefreshTokenException("Refresh Token is not in database or has expired"));


        User user = token.getUser();
        String jwtToken = jwtUtils.generateJwtToken(authentication, user.getRole().getName());
        return new JwtRefreshResponseDto(
                jwtToken,
                refreshToken
        );
    }
}
