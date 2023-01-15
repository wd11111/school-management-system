package pl.schoolmanagementsystem.common.model;

import lombok.*;

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
    private Long id;

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
