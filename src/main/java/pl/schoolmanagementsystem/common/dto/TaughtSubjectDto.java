package pl.schoolmanagementsystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaughtSubjectDto {

    private String subject;
    private String teacherName;
    private String teacherSurname;
}
