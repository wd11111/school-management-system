package pl.schoolmanagementsystem.common.schoolClass.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInClassRequestDto {

    @Min(value = 1, message = "{not.null.message}")
    private long teacherId;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String taughtSubject;
}
