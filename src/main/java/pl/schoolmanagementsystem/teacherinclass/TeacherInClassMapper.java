package pl.schoolmanagementsystem.teacherinclass;

import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassOutputDto;

@Component
public class TeacherInClassMapper {

    public TeacherInClassOutputDto mapTeacherInClassInputToOutputDto(
            TeacherInClassInputDto teacherInput, String subjectName) {
        return new TeacherInClassOutputDto(
                teacherInput.getTeacherId(), teacherInput.getTaughtSubject(), subjectName);
    }
}
