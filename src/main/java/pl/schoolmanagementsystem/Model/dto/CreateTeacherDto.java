package pl.schoolmanagementsystem.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.schoolmanagementsystem.Model.SchoolSubject;
import pl.schoolmanagementsystem.Model.TeacherInClass;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeacherDto {

    private String name;

    private String surname;

    private Set<SchoolSubject> taughtSubjects;

    private Set<TeacherInClass> teacherInClasses;

}
