package pl.schoolmanagementsystem.schoolsubject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubjectAndClassOutputDto {

    private String schoolSubject;
    private String schoolClass;
}
