package pl.schoolmanagementsystem.common.mark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarkInputDto {

    @Max(value = 6, message = "{mark.rank.message}")
    @Min(value = 1, message = "{mark.rank.message}")
    @NotNull(message = "{not.null.message}")
    private int mark;

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String subject;

}
