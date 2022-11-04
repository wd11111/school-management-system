package pl.schoolmanagementsystem.model.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MarkInputDto {

    private int teacherId;
    private int mark;
    private String subject;

}
