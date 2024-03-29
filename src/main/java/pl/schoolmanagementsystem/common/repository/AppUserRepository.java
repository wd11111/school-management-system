package pl.schoolmanagementsystem.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.schoolmanagementsystem.common.model.AppUser;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByToken(String token);

    @Query("SELECT ap FROM AppUser ap LEFT JOIN FETCH ap.roles WHERE ap.email=?1")
    Optional<AppUser> findByUserEmail(String email);

    boolean existsByEmail(String email);
}

