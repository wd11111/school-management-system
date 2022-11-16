package pl.schoolmanagementsystem.admin.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.email.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {

}
