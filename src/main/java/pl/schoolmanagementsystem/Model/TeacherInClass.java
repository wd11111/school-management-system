package pl.schoolmanagementsystem.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teacherInClassId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_name")
    private SchoolSubject taughtSubject;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "teacherInClass_schoolClasses",
            joinColumns = {
                    @JoinColumn(name = "teacher_in_class_id", referencedColumnName = "teacherInClassId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "school_class_name", referencedColumnName = "schoolClassName")})
    private Set<SchoolClass> taughtClasses = new HashSet<>();
}
