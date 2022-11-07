package pl.schoolmanagementsystem.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teacherId;

    private String name;

    private String surname;

    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email", nullable = false)
    private Email email;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subjects",
            joinColumns = {
                    @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "subject_name", referencedColumnName = "subjectName")})
    private Set<SchoolSubject> taughtSubjects;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TeacherInClass> teacherInClasses;
}
