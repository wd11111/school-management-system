package pl.schoolmanagementsystem.common.model;

import lombok.*;

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
    @EqualsAndHashCode.Include
    private String name;

    @ManyToMany(mappedBy = "taughtSubjects")
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "taughtSubject")
    private Set<TeacherInClass> teachersInClasses = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject")
    private List<Mark> marks = new ArrayList<>();

}
