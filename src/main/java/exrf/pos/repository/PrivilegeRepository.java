package exrf.pos.repository;

import exrf.pos.model.Privilege;
import exrf.pos.model.enums.EPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByPrivilege(EPrivilege privilege);
}
