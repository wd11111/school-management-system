package pl.schoolmanagementsystem.common.student;

import lombok.*;
import pl.schoolmanagementsystem.common.mark.Mark;
import pl.schoolmanagementsystem.common.user.AppUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String surname;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userEmail")
    private AppUser appUser;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "studentId")
    private List<Mark> marks = new ArrayList<>();

    private String schoolClass;

    public void addMark(Mark mark) {
        marks.add(mark);
    }
}
