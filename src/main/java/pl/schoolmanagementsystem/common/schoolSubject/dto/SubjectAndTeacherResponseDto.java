package pl.schoolmanagementsystem.common.schoolSubject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubjectAndTeacherResponseDto {

    private String subject;
    private String teacherName;
    private String teacherSurname;
}
