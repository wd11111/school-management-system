package pl.schoolmanagementsystem.schoolsubject.model;

import lombok.*;
import pl.schoolmanagementsystem.mark.model.Mark;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
public class SchoolSubject {

    @Id
    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String name;

    @ManyToMany(mappedBy = "taughtSubjects", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "taughtSubject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TeacherInClass> teachersInClasses = new HashSet<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mark> marks = new ArrayList<>();

}
