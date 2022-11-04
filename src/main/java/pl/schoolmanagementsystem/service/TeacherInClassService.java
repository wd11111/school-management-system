package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.*;
import pl.schoolmanagementsystem.mapper.TeacherMapper;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.TeacherInClass;
import pl.schoolmanagementsystem.model.dto.input.TeacherInClassInputDto;
import pl.schoolmanagementsystem.model.dto.output.TeacherInClassOutputDto;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.repository.TeacherRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherInClassService {

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    public TeacherInClassOutputDto addTeacherInClassToSchoolClass(TeacherInClassInputDto teacherInClassInputDto, String schoolClassName) {
        Teacher teacherObject = findTeacher(teacherInClassInputDto.getTeacherId());
        SchoolClass schoolClass = findSchoolClass(schoolClassName);
        SchoolSubject schoolSubject = findSchoolSubject(teacherInClassInputDto.getTaughtSubject());
        makeSureIfTeacherTeachThisSubject(teacherObject, schoolSubject);
        checkIfThisClassAlreadyHasTeacherOfThisSubject(schoolClass, schoolSubject);
        teacherInClassRepository.save(buildTeacherInClass(teacherObject, schoolSubject, schoolClass));
        return TeacherMapper.mapTeacherInClassInputDtoToOutput(teacherInClassInputDto, schoolClassName);
    }

    private Teacher findTeacher(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchTeacherException(id));
    }

    private SchoolClass findSchoolClass(String name) {
        return schoolClassRepository.findBySchoolClassName(name)
                .orElseThrow(() -> new NoSuchSchoolClassException(name));
    }

    private SchoolSubject findSchoolSubject(String name) {
        return schoolSubjectRepository.findBySubjectName(name)
                .orElseThrow(() -> new NoSuchSchoolSubjectException(name));
    }

    private void makeSureIfTeacherTeachThisSubject(Teacher teacher, SchoolSubject schoolSubject) {
        boolean doesTeacherTeachTheSubject = teacher.getTaughtSubjects().contains(schoolSubject);
        if (!doesTeacherTeachTheSubject) {
            throw new TeacherDoesNotTeachSubjectException(teacher, schoolSubject);
        }
    }

    private TeacherInClass buildTeacherInClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        TeacherInClass teacherInClass = getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElse(new TeacherInClass());
        teacherInClass.getTaughtClasses().add(schoolClass);
        if (!doesTeacherAlreadyHaveEquivalent(teacher, schoolSubject)) {
            teacherInClass.setTeacher(teacher);
            teacherInClass.setTaughtSubject(schoolSubject);
        }
        return teacherInClass;
    }

    private Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                                       SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private boolean doesTeacherAlreadyHaveEquivalent(Teacher teacher, SchoolSubject schoolSubject) {
        return teacherInClassRepository.existsByTeacherAndTaughtSubject(teacher, schoolSubject);
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