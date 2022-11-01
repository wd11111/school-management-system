package pl.schoolmanagementsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classId;

    private String name;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    private Set<Student> students;
}
