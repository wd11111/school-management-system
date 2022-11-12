package pl.schoolmanagementsystem.teacherinclass;

import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassOutputDto;

public class TeacherInClassMapper {

    public static TeacherInClassOutputDto mapTeacherInClassInputToOutputDto(
            TeacherInClassInputDto teacherInput, String subjectName) {
        return new TeacherInClassOutputDto(
                teacherInput.getTeacherId(), teacherInput.getTaughtSubject(), subjectName);
    }
}
