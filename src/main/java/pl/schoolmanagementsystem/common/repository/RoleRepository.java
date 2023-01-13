package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
