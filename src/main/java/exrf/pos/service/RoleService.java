package exrf.pos.service;

import exrf.pos.dto.requests.RoleDto;
import exrf.pos.exception.InvalidRoleException;
import exrf.pos.model.Role;
import exrf.pos.model.enums.ERole;
import exrf.pos.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role getOne(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(
                () -> new RuntimeException("Role not found")
        );
    }

    public Page<Role> getAllRoles(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return roleRepository.findAll(pageable);
    }

    public List<Role> updateAllRoles(List<RoleDto> updateRequests) {
        List<Role> updatedRoles = new ArrayList<>();

        for (RoleDto request : updateRequests) {
            Role role = roleRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + request.getId()));
            role.setRole(request.getName());
            updatedRoles.add(roleRepository.save(role));
        }

        return updatedRoles;
    }

    public void deleteAllRoles(List<Long> roleIds) {
        List<Role> rolesToDelete = roleRepository.findAllById(roleIds);
        if (rolesToDelete.size() != roleIds.size()) {
            throw new RuntimeException("Some roles were not found for deletion.");
        }

        roleRepository.deleteAll(rolesToDelete);
    }
}
