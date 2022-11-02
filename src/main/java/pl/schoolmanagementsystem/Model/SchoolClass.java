package pl.schoolmanagementsystem.Model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolClass implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    private String schoolClassName;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "taughtClasses", cascade = CascadeType.PERSIST)
    private Set<TeacherInClass> teachersInClass = new HashSet<>();
}
