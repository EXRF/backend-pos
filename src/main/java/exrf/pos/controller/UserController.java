package exrf.pos.controller;

import exrf.pos.dto.responses.UserDto;
import exrf.pos.model.User;
import exrf.pos.service.UserService;
import exrf.pos.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getDetail(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDto user = userService.get(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.responseSuccess(UserDto.class, user, "Success"));
    }
}
