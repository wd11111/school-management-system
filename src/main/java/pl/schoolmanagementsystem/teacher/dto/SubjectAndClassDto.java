package pl.schoolmanagementsystem.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SubjectAndClassDto {

    private String schoolSubject;
    private String schoolClass;
}
