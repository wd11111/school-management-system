package pl.schoolmanagementsystem.common.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolClass {

    @Id
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "schoolClass")
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "taughtClasses", cascade = CascadeType.PERSIST)
    private Set<TeacherInClass> teachersInClass = new HashSet<>();
}
