package exrf.pos.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import exrf.pos.model.Privilege;
import exrf.pos.model.RolePrivilege;
import exrf.pos.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    @Getter
    private Set<Privilege> privileges;

//    @Getter
//    private Set<String> modules;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getName())
        );

        Set<Privilege> privileges = user.getRole().getRolePrivileges()
                .stream()
                .map(RolePrivilege::getPrivilege)
                .collect(Collectors.toSet());
//
//        Optional<Set<String>> modules = Optional.of(user.getRole().getRolePrivileges())
//                .map(rolePrivileges -> rolePrivileges.stream()
//                        .map(rolePrivilege -> rolePrivilege.getModule().getName())
//                        .collect(Collectors.toSet()));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                privileges,
//                modules
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UserDetailsImpl userDetails = (UserDetailsImpl) obj;
        return Objects.equals(id, userDetails.id);
    }
}
