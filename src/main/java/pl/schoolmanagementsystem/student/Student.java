package pl.schoolmanagementsystem.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.schoolmanagementsystem.Mark.Mark;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    @OneToMany(mappedBy = "studentId", cascade = CascadeType.ALL)
    private List<Mark> marks;


}
