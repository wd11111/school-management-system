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
public class TeacherInClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = LAZY)
    private Teacher teacher;

    private String taughtSubject;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<SchoolClass> taughtClasses = new HashSet<>();

    public void assignToClass(SchoolClass schoolClass) {
        if (taughtClasses.contains(schoolClass)) {
            throw new IllegalArgumentException();
        }
        taughtClasses.add(schoolClass);
    }

    public void removeFromClass(SchoolClass schoolClass) {
        if (!taughtClasses.contains(schoolClass)) {
            throw new IllegalArgumentException();
        }
        taughtClasses.remove(schoolClass);
    }
}
