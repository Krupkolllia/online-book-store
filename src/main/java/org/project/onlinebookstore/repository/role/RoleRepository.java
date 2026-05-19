package org.project.onlinebookstore.repository.role;

import java.util.Optional;
import org.project.onlinebookstore.model.user.Role;
import org.project.onlinebookstore.model.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
