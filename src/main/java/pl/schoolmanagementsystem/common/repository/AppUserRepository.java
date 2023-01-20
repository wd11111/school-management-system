package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.model.AppUser;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByToken(String token);

    /*@Query("SELECT ap FROM AppUser ap LEFT JOIN FETCH ap.roles WHERE ap.userEmail=?1")
    Optional<AppUser> findByUserEmail(String email);*/

    Optional<AppUser> findByUserEmail(String email);
}

