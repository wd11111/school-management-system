package pl.schoolmanagementsystem.schoolClass.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddOrRemoveTeacherInClassDto {

    @Min(value = 1, message = "{not.null.message}")
    private Long teacherId;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String taughtSubject;
}
