package pl.schoolmanagementsystem.teacherinclass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubjectService;
import pl.schoolmanagementsystem.teacher.Teacher;
import pl.schoolmanagementsystem.teacher.TeacherService;
import pl.schoolmanagementsystem.teacher.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.teacherinclass.utils.TeacherInClassBuilder;
import pl.schoolmanagementsystem.teacherinclass.utils.TeacherInClassMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherInClassService {

    private final SchoolClassService schoolClassService;

    private final SchoolSubjectService schoolSubjectService;

    private final TeacherService teacherService;

    private final TeacherInClassRepository teacherInClassRepository;

    public TeacherInClassOutputDto addTeacherInClassToSchoolClass(TeacherInClassInputDto teacherInClassInputDto, String schoolClassName) {
        Teacher teacher = teacherService.findById(teacherInClassInputDto.getTeacherId());
        SchoolClass schoolClass = schoolClassService.findById(schoolClassName);
        SchoolSubject schoolSubject = schoolSubjectService.findByName(teacherInClassInputDto.getTaughtSubject());
        teacherService.makeSureIfTeacherTeachesThisSubject(teacher, schoolSubject);
        schoolClassService.checkIfThisClassAlreadyHasTeacherOfThisSubject(schoolClass, schoolSubject);
        teacherInClassRepository.save(buildTeacherInClass(teacher, schoolSubject, schoolClass));
        return TeacherInClassMapper.mapTeacherInClassInputToOutputDto(teacherInClassInputDto, schoolClassName);
    }

    public void makeSureIfTeacherTeachesThisClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        if (teacher.isAdmin()) {
            return;
        }
        TeacherInClass teacherInClass = getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElseThrow(() -> new TeacherDoesNotTeachClassException(schoolSubject, schoolClass));
        boolean doesTeacherTeachThisClass = teacherInClass.getTaughtClasses().contains(schoolClass);
        if (!doesTeacherTeachThisClass) {
            throw new TeacherDoesNotTeachClassException(schoolSubject, schoolClass);
        }
    }

    public Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                                      SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private TeacherInClass buildTeacherInClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        return TeacherInClassBuilder.build(teacher, schoolSubject, schoolClass, this);
    }
}