package pl.schoolmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SchoolSubject {

    @Id
    @Column(nullable = false, unique = true)
    private String subjectName;

    @JsonIgnore
    @OneToMany(mappedBy = "taughtSubject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TeacherInClass> teachersInClasses = new HashSet<>();


}
