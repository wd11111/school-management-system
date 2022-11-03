package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.TeacherInClass;
import pl.schoolmanagementsystem.model.dto.TeacherInClassDto;
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

    public TeacherInClass addTeacherInClassToSchoolClass(TeacherInClassDto teacherInClassDto, String schoolClassName) {
        Teacher teacherObject = teacherRepository.findById(teacherInClassDto.getTeacherId())
                .orElseThrow();
        SchoolClass schoolClass = schoolClassRepository.findBySchoolClassName(schoolClassName)
                .orElseThrow();
        SchoolSubject schoolSubject = schoolSubjectRepository.findBySubjectName(teacherInClassDto.getTaughtSubject())
                .orElseThrow();
        if (!doesTeacherTeachTheSubject(teacherObject, schoolSubject)) {
            throw new RuntimeException();
        }
        checkIfThisClassAlreadyHasTeacherOfThisSubject(schoolClass, schoolSubject)
                .ifPresent(teacher -> {
                    throw new RuntimeException(); //new teacher already teaches
                });
        return teacherInClassRepository.save(createTeacherInClass(teacherObject, schoolSubject, schoolClass));
    }

    private boolean doesTeacherTeachTheSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }

    private TeacherInClass createTeacherInClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        TeacherInClass teacherInClass = getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElse(new TeacherInClass());
        teacherInClass.getTaughtClasses().add(schoolClass);
        if (!doesTeacherAlreadyHaveEquivalent(teacher, schoolSubject)) {
            teacherInClass.setTeacher(teacher);
            teacherInClass.setTaughtSubject(schoolSubject);
        }
        return teacherInClass;
    }

    protected Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                                         SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private boolean doesTeacherAlreadyHaveEquivalent(Teacher teacher, SchoolSubject schoolSubject) {
        return teacherInClassRepository.existsByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private Optional<TeacherInClass> checkIfThisClassAlreadyHasTeacherOfThisSubject(SchoolClass schoolClass,
                                                                                    SchoolSubject schoolSubject) {
        return schoolClass.getTeachersInClass().stream()
                .filter(teacher -> teacher.getTaughtSubject().equals(schoolSubject))
                .findFirst();
    }

}