package pl.schoolmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class MarkDto {

    @Max(value = 6, message = "{mark.rank.message}")
    @Min(value = 1, message = "{mark.rank.message}")
    @NotNull(message = "{not.null.message}")
    private byte mark;// todo tu też dlaczego ten byte, bo mnie to ciekawi? :D

    @NotNull(message = "{not.null.message}")
    @NotBlank(message = "{not.blank.message}")
    private String subject;

}
