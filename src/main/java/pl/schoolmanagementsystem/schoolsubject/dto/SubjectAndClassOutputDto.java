package pl.schoolmanagementsystem.schoolsubject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectAndClassOutputDto {

    private String schoolSubject;
    private String schoolClass;
}