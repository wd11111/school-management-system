package pl.schoolmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    // TODO
    //  Zamiast robić milion różnych response,
    //  lepiej zrobić jedno które będzie miało wiele pól,
    //  w którym nie każde pole będzie po prostu używane,
    //  wiem, że wtedy nie będziesz mógł wykorzystać np @NotNull, ale w pracy StudentRequest1,2,3 by nie przeszło
    //  ewentualnie możesz pozmieniać nazwy tak, aby to DTO bardziej oddawało to czym jest.
}
