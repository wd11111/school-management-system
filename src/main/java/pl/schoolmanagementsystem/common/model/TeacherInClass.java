package pl.schoolmanagementsystem.common.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
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
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = LAZY)
    private Teacher teacher;

    private String taughtSubject;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = EAGER)
    private Set<SchoolClass> taughtClasses = new HashSet<>();
}
