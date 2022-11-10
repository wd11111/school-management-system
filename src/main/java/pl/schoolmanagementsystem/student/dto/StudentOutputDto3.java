package pl.schoolmanagementsystem.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentOutputDto3 {

    private int studentId;
    private String name;
    private String surname;
    private List<Integer> marks;
}
