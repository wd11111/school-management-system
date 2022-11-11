package pl.schoolmanagementsystem.schoolclass;

import lombok.*;
import pl.schoolmanagementsystem.student.Student;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClass;

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
