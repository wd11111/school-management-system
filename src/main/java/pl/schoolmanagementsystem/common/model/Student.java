package pl.schoolmanagementsystem.common.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "appUserId", referencedColumnName = "id")
    private AppUser appUser;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "studentId")
    private List<Mark> marks = new ArrayList<>();

    private String schoolClass;

    public void giveMark(Mark mark) {
        marks.add(mark);
    }
}
