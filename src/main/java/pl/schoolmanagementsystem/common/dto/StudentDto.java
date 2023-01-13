package pl.schoolmanagementsystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private Long studentId;
    private String name;
    private String surname;
}
