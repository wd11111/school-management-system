package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
