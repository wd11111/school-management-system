package pl.schoolmanagementsystem.student.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateStudentDto {

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    @Email(message = "invalid format")
    private String email;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String name;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String surname;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String schoolClass;

    @Past(message = "{date.past.message}")
    private LocalDate birthDate;

}
