package pl.schoolmanagementsystem.common.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByToken(String token);

    @Query("select ap from AppUser ap left join fetch ap.roles where ap.userEmail=?1")
    Optional<AppUser> findByUserEmail(String email);
}
