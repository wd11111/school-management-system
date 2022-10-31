package pl.schoolmanagementsystem.Mark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.schoolmanagementsystem.student.Student;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int mark;

    @ManyToOne
    @JoinColumn(name = "studentId", referencedColumnName = "id")
    private Student studentId;
}
