package exrf.pos.service;

import exrf.pos.dto.responses.UserDto;
import exrf.pos.model.User;
import exrf.pos.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserDto get(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDto(user.getUsername(), user.getEmail(), user.getRoles().stream().map(role -> role.getRole().name()).collect(Collectors.toList()));
    }
}
