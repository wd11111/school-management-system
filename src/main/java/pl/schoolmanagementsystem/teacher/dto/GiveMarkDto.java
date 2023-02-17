package pl.schoolmanagementsystem.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.schoolmanagementsystem.common.model.MarkEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class GiveMarkDto {

    @NotNull(message = "{not.null.message}")
    private MarkEnum mark;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String subject;

}
