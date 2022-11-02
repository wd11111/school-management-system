package pl.schoolmanagementsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "teacher_subjects",
            joinColumns = {
                    @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "subject_name", referencedColumnName = "subjectName")})
    private Set<SchoolSubject> taughtSubjects;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<TeacherInClass> teacherInClasses = new HashSet<>();
}
