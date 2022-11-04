package pl.schoolmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SchoolSubject {

    @Id
    @Column(nullable = false, unique = true)
    private String subjectName;

    @JsonIgnore
    @ManyToMany(mappedBy = "taughtSubjects", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Teacher> teachers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "taughtSubject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TeacherInClass> teachersInClasses = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mark> marks = new ArrayList<>();

}
