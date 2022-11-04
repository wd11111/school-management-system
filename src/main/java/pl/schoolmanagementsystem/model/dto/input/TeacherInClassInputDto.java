package pl.schoolmanagementsystem.model.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInClassInputDto {

    private int teacherId;
    private String taughtSubject;
}
