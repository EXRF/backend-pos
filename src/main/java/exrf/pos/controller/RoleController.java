package exrf.pos.controller;

import exrf.pos.dto.requests.RoleDto;
import exrf.pos.dto.responses.CommonResponseDto;
import exrf.pos.model.Role;
import exrf.pos.service.RoleService;
import exrf.pos.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        Role role = roleService.getOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseUtil.responseSuccess(Role.class, role, "Success")
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page must be 0 or greater, and pageSize must be greater than 0.");
        }

        Page<Role> roles = roleService.getAllRoles(page, pageSize);
        CommonResponseDto.Metadata metadata = new CommonResponseDto.Metadata(
                roles.getPageable().getPageNumber(),
                roles.getPageable().getPageSize(),
                roles.getTotalElements(),
                roles.getTotalPages()
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseUtil.responseSuccess(roles.getContent(), metadata, "Success"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOne(@PathVariable("id") Long id, @RequestBody String name) {
        Role role = roleService.updateOne(id, name);
        return ResponseEntity.ok(
                ResponseUtil.responseSuccess(Role.class, role, "role " + " updated successfully.")
        );
    }

    @PutMapping
    public ResponseEntity<CommonResponseDto<List<Role>>> updateAll(@RequestBody List<RoleDto> updateRequests) {
        if (updateRequests == null || updateRequests.isEmpty()) {
            throw new IllegalArgumentException("Update requests must not be null or empty.");
        }

        List<Role> updatedRoles = roleService.updateAllRoles(updateRequests);

        return ResponseEntity.ok(
                ResponseUtil.responseSuccess(updatedRoles, null, "All roles updated successfully.")
        );
    }

}
