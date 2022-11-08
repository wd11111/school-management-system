package pl.schoolmanagementsystem.model.dto.input;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentInputDto {

    private String name;
    private String surname;
    private String password;
    private String schoolClassName;
    private String email;
}
