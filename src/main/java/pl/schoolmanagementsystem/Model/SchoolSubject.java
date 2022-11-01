package pl.schoolmanagementsystem.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SchoolSubject {

    @Id
    @Column(nullable = false, unique = true)
    private String subjectName;

    @ManyToMany(mappedBy = "taughtSubjects", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Teacher> teachers;
}
