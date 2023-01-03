package pl.schoolmanagementsystem.common.teacher;

import lombok.*;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.user.AppUser;

import javax.persistence.*;
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
    private long id;

    private String name;

    private String surname;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userEmail")
    private AppUser appUser;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<SchoolSubject> taughtSubjects;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<TeacherInClass> teacherInClasses;

    public void addSubject(SchoolSubject schoolSubject) {
        taughtSubjects.add(schoolSubject);
    }
}
