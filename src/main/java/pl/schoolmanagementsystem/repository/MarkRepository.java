package pl.schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.schoolmanagementsystem.model.Mark;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
}
