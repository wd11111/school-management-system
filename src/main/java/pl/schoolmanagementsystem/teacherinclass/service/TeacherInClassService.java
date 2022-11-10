package pl.schoolmanagementsystem.teacherinclass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.service.TeacherService;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;
import pl.schoolmanagementsystem.teacherinclass.repository.TeacherInClassRepository;
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
        SchoolClass schoolClass = schoolClassService.findSchoolClass(schoolClassName);
        SchoolSubject schoolSubject = schoolSubjectService.findByName(teacherInClassInputDto.getTaughtSubject());
        teacherService.makeSureIfTeacherTeachesThisSubject(teacher, schoolSubject);
        schoolClassService.checkIfThisClassAlreadyHasTeacherOfThisSubject(schoolClass, schoolSubject);
        teacherInClassRepository.save(buildTeacherInClass(teacher, schoolSubject, schoolClass));
        return TeacherInClassMapper.mapTeacherInClassInputToOutputDto(teacherInClassInputDto, schoolClassName);
    }

    public Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                                      SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private TeacherInClass buildTeacherInClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        return TeacherInClassBuilder.buildTeacherInClass(teacher, schoolSubject, schoolClass, this);
    }
}