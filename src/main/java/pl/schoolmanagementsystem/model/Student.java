package pl.schoolmanagementsystem.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class Student {

    @Id
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence",
            allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    private int studentId;

    private String name;

    private String surname;

    private String password;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mark> marks = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "school_class_name")
    private SchoolClass schoolClass;
}
