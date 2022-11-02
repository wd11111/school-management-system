package pl.schoolmanagementsystem.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teacherId;

    private String name;

    private String surname;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subjects",
            joinColumns = {
                    @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "subject_name", referencedColumnName = "subjectName")})
    private Set<SchoolSubject> taughtSubjects = new HashSet<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TeacherInClass> teacherInClasses = new HashSet<>();
}
