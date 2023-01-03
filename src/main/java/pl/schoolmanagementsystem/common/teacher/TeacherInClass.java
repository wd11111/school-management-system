package pl.schoolmanagementsystem.common.teacher;

import lombok.*;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = LAZY)
    private Teacher teacher;

    private String taughtSubject;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<SchoolClass> taughtClasses = new HashSet<>();
}
