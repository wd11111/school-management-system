package pl.schoolmanagementsystem.model.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectAndTeacherOutputDto {

    private String subject;
    private String teacherName;
    private String teacherSurname;
}
