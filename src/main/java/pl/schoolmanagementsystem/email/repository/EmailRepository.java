package pl.schoolmanagementsystem.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.email.model.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {

}
