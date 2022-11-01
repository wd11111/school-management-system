package pl.schoolmanagementsystem.Model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@EqualsAndHashCode.Include
    private int teacherId;

    private String name;

    private String surname;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "teacher_subjects",
            joinColumns = {
                    @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "subject_name", referencedColumnName = "subjectName")})
    Set<SchoolSubject> taughtSubjects;
}
