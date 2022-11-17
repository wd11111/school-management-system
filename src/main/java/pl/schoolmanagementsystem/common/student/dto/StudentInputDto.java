package pl.schoolmanagementsystem.common.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudentInputDto {

    private String name;
    private String surname;
    private String schoolClassName;
    private String email;
}
