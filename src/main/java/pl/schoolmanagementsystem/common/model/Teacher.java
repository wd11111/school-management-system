package pl.schoolmanagementsystem.common.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "appUserId", referencedColumnName = "id")
    private AppUser appUser;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<SchoolSubject> taughtSubjects;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<TeacherInClass> teacherInClasses = new HashSet<>();

    public void assignSubject(SchoolSubject schoolSubject) {
        taughtSubjects.add(schoolSubject);
    }
}
