package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.model.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {

}
