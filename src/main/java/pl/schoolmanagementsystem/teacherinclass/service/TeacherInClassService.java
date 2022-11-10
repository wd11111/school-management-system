package pl.schoolmanagementsystem.teacherinclass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.mapper.TeacherMapper;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.repository.TeacherRepository;
import pl.schoolmanagementsystem.teacher.service.TeacherService;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.teacherinclass.dto.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;
import pl.schoolmanagementsystem.teacherinclass.repository.TeacherInClassRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherInClassService {

    private final SchoolClassService schoolClassService;

    private final SchoolSubjectService schoolSubjectService;

    private final TeacherService teacherService;

    private final TeacherInClassRepository teacherInClassRepository;

    private final TeacherInClassBuilder teacherInClassBuilder;

    public TeacherInClassOutputDto addTeacherInClassToSchoolClass(TeacherInClassInputDto teacherInClassInputDto, String schoolClassName) {
        Teacher teacherObject = findTeacher(teacherInClassInputDto.getTeacherId());
        SchoolClass schoolClass = findSchoolClass(schoolClassName);
        SchoolSubject schoolSubject = findSchoolSubject(teacherInClassInputDto.getTaughtSubject());
        makeSureIfTeacherTeachesThisSubject(teacherObject, schoolSubject);
        checkIfThisClassAlreadyHasTeacherOfThisSubject(schoolClass, schoolSubject);
        teacherInClassRepository.save(buildTeacherInClass(teacherObject, schoolSubject, schoolClass));
        return TeacherMapper.mapTeacherInClassInputToOutputDto(teacherInClassInputDto, schoolClassName);
    }

    public Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                                      SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private Teacher findTeacher(int id) {
        return teacherService.findTeacherById(id);
    }

    private SchoolClass findSchoolClass(String name) {
        return schoolClassService.findSchoolClass(name);
    }

    private SchoolSubject findSchoolSubject(String name) {
        return schoolSubjectService.findSchoolSubject(name);
    }

    private void makeSureIfTeacherTeachesThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (!doesTeacherTeachTheSubject(teacher, schoolSubject)) {
            throw new TeacherDoesNotTeachSubjectException(teacher, schoolSubject);
        }
    }

    private boolean doesTeacherTeachTheSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }

    private TeacherInClass buildTeacherInClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        return teacherInClassBuilder.buildTeacherInClass(teacher, schoolSubject, schoolClass);
    }

    private void checkIfThisClassAlreadyHasTeacherOfThisSubject(SchoolClass schoolClass,
                                                                SchoolSubject schoolSubject) {
        schoolClass.getTeachersInClass().stream()
                .filter(teacher -> teacher.getTaughtSubject().equals(schoolSubject))
                .findFirst()
                .ifPresent(teacher -> {
                    throw new ClassAlreadyHasTeacherException(teacher, schoolSubject, schoolClass);
                });
    }

}