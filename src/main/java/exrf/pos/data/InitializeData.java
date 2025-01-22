package exrf.pos.data;

import exrf.pos.model.Privilege;
import exrf.pos.model.Role;
import exrf.pos.model.RolePrivilege;
import exrf.pos.model.enums.EPrivilege;
import exrf.pos.repository.PrivilegeRepository;
import exrf.pos.repository.RolePrivilegeRepository;
import exrf.pos.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("dev") // Ensure this runs only in the "dev" profile
public class InitializeData implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RolePrivilegeRepository rolePrivilegeRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (privilegeRepository.count() == 0) {
            Arrays.stream(EPrivilege.values()).forEach(ePrivilege -> {
                if(privilegeRepository.findByPrivilege(ePrivilege).isEmpty()) {
                    Privilege privilege = new Privilege();
                    privilege.setPrivilege(ePrivilege);
                    privilegeRepository.save(privilege);
                }
            });

            System.out.println("Privileges initialized");
        }

        if (roleRepository.count() == 0) {
            List<Privilege> allPrivileges = privilegeRepository.findAll();

            // Create ADMIN role with all privileges
            createRoleWithPrivileges("ADMIN", allPrivileges);

            // Create USER role with READ privilege only
            Privilege readPrivilege = privilegeRepository.findByPrivilege(EPrivilege.READ)
                    .orElseThrow(() -> new RuntimeException("READ Privilege not found"));
            createRoleWithPrivileges("USER", List.of(readPrivilege));

            // Create ANONYMOUS role with READ privilege only
            createRoleWithPrivileges("ANONYMOUS", List.of(readPrivilege));

            System.out.println("Roles and RolePrivileges initialized.");
        }
    }

    private void createRoleWithPrivileges(String name, List<Privilege> privileges) {
        Role role = new Role();
        role.setName(name);
        role = roleRepository.save(role);

        Role finalRole = role;
        List<RolePrivilege> rolePrivileges = privileges.stream()
                .map(privilege -> {
                    RolePrivilege rolePrivilege = new RolePrivilege();
                    rolePrivilege.setRole(finalRole);
                    rolePrivilege.setPrivilege(privilege);
                    return rolePrivilege;
                })
                .collect(Collectors.toList());

        rolePrivilegeRepository.saveAll(rolePrivileges);
    }
}
