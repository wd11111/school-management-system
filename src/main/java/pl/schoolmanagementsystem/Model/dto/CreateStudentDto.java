package pl.schoolmanagementsystem.Model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentDto {

    private String name;
    private String surname;
    private String schoolClassName;
}
