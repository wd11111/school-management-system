package pl.schoolmanagementsystem.schoolsubject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.schoolclass.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndTeacherOutputDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolSubjectFacade {

    private final SchoolSubjectService schoolSubjectService;

    private final SchoolClassService schoolClassService;

    public List<SubjectAndTeacherOutputDto> getAllSubjectsForSchoolClass(String schoolClassName) {
        schoolClassService.checkIfClassExists(schoolClassName);
        return schoolSubjectService.getAllSubjectsForSchoolClass(schoolClassName);
    }
}
