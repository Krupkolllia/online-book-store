package org.project.onlinebookstore.repository;

import java.util.Optional;
import org.project.onlinebookstore.model.Role;
import org.project.onlinebookstore.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
