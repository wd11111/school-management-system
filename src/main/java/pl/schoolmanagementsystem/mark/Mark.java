package pl.schoolmanagementsystem.mark;

import lombok.*;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.student.Student;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int mark;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Student student;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private SchoolSubject subject;
}
