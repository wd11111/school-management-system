package pl.schoolmanagementsystem.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeacherInClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teacherInClassId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "subject_name")
    private SchoolSubject taughtSubject;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "teacherInClass_schoolClasses",
            joinColumns = {
                    @JoinColumn(name = "teacher_in_class_id", referencedColumnName = "teacherInClassId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "class_id", referencedColumnName = "classId")})
    private Set<SchoolClass> taughtClasses = new HashSet<>();
}
