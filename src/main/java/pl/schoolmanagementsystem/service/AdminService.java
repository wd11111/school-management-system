package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.*;
import pl.schoolmanagementsystem.model.dto.StudentDto;
import pl.schoolmanagementsystem.model.dto.TeacherDto;
import pl.schoolmanagementsystem.model.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.model.dto.TextDto;
import pl.schoolmanagementsystem.repository.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final TeacherRepository teacherRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolClass createSchoolClass(TextDto schoolClassName) {
        if (doesSchoolClassAlreadyExistsInDatabase(schoolClassName.getPlainText())) {
            throw new RuntimeException();
        }
        return schoolClassRepository.save(SchoolClass.builder()
                .schoolClassName(schoolClassName.getPlainText())
                .build());
    }

    public Student createStudent(StudentDto studentDto) {
        SchoolClass schoolClass = schoolClassRepository.findBySchoolClassName(studentDto.getSchoolClassName())
                .orElseThrow();
        return studentRepository.save(Student.builder()
                .name(studentDto.getName())
                .surname(studentDto.getSurname())
                .schoolClass(schoolClass)
                .build());
    }

    public Teacher createTeacher(TeacherDto teacherDto) {
        Teacher teacher = new Teacher();
        teacher.setName(teacherDto.getName());
        teacher.setSurname(teacherDto.getSurname());
        addTaughtSubjectsToTeacher(teacher, teacherDto.getTaughtSubjects());
        return teacherRepository.save(teacher);
    }

    public TeacherInClass addExistingTeacherToClass(TeacherInClassDto teacherInClassDto) {
        Teacher teacherObject = teacherRepository.findById(teacherInClassDto.getTeacherId())
                .orElseThrow();
        SchoolSubject schoolSubject = schoolSubjectRepository.findBySubjectName(teacherInClassDto.getTaughtSubject())
                .orElseThrow();
        if (!doesTeacherTeachTheSubject(teacherObject, schoolSubject)) {
            throw new RuntimeException();//teacherObject nie uczy tego przedmiotu exception
        }
        SchoolClass schoolClass = schoolClassRepository.findBySchoolClassName(teacherInClassDto.getSchoolClassName())
                .orElseThrow();

        findTeacherInClassWhichAlreadyTeachesThisSubject(schoolClass, schoolSubject)
                .ifPresent(teacher -> {
                    throw new RuntimeException(); //new teacher already teaches
                });
        return teacherInClassRepository.save(createTeacherInClass(teacherObject, schoolSubject, schoolClass));
    }

    public SchoolSubject addSchoolSubject(TextDto subjectName) {
        if (doesSubjectAlreadyExistsInDatabase(subjectName.getPlainText())) {
            throw new RuntimeException();
        }
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setSubjectName(subjectName.getPlainText());
        return schoolSubjectRepository.save(schoolSubject);
    }

    protected Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                                         SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private Optional<TeacherInClass> findTeacherInClassWhichAlreadyTeachesThisSubject(SchoolClass schoolClass,
                                                                                      SchoolSubject schoolSubject) {
        return schoolClass.getTeachersInClass().stream()
                .filter(teacher -> teacher.getTaughtSubject().equals(schoolSubject))
                .findFirst();
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

    private void addTaughtSubjectsToTeacher(Teacher teacher, Set<String> subjects) {
        Set<SchoolSubject> subjectObjects = transformSetOfStringsToSetOfSchoolClassObjects(subjects);
        subjectObjects.forEach(subject ->
                teacher.getTaughtSubjects().add(subject));
    }

    private Set<SchoolSubject> transformSetOfStringsToSetOfSchoolClassObjects(Set<String> subjects) {
        return subjects.stream()
                .filter(subject -> doesSubjectAlreadyExistsInDatabase(subject))
                .map(subject -> schoolSubjectRepository.findBySubjectName(subject)
                        .orElse(null))
                .collect(Collectors.toSet());
    }

    private boolean doesTeacherTeachTheSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }

    private boolean doesTeacherAlreadyHaveEquivalent(Teacher teacher, SchoolSubject schoolSubject) {
        return teacherInClassRepository.existsByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private boolean doesSubjectAlreadyExistsInDatabase(String subjectName) {
        return schoolSubjectRepository.existsBySubjectName(subjectName);
    }

    private boolean doesSchoolClassAlreadyExistsInDatabase(String schoolClassName) {
        return schoolClassRepository.existsBySchoolClassName(schoolClassName);
    }
}
