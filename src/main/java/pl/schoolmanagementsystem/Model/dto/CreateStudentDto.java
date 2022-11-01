package pl.schoolmanagementsystem.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateStudentDto {

    private String name;
    private String surname;
    private String schoolClassName;
}
