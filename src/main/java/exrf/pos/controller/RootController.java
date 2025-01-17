package exrf.pos.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class RootController {

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")  // Only user has role ROLE_ADMIN can access this
    public String root() {
        return "OK";
    }
}
