package pl.schoolmanagementsystem.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto3 {

    private long studentId;
    private String name;
    private String surname;
    private List<Byte> marks;
}