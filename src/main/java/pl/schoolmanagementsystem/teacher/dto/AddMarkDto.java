package pl.schoolmanagementsystem.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class AddMarkDto {

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String mark;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String subject;

}
