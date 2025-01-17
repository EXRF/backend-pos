package exrf.pos.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")  // Only user has role ROLE_ADMIN can access this
    public String root() {
        return "OK";
    }
}
