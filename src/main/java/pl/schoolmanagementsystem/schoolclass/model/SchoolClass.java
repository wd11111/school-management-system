package pl.schoolmanagementsystem.schoolclass.model;

import lombok.*;
import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;

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
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "taughtClasses", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<TeacherInClass> teachersInClass = new HashSet<>();
}
