package pl.schoolmanagementsystem.common.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudentRequestDto {

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String name;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String surname;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String schoolClassName;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String email;

}
