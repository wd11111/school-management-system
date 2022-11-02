package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.*;
import pl.schoolmanagementsystem.model.dto.StudentDto;
import pl.schoolmanagementsystem.model.dto.TeacherDto;
import pl.schoolmanagementsystem.model.dto.NameDto;
import pl.schoolmanagementsystem.model.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.repository.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final TeacherRepository teacherRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolClass createSchoolClass(NameDto schoolClassNameDto) {
        if (doesSchoolClassAlreadyExistsInDatabase(schoolClassNameDto.getPlainText())) {
            throw new RuntimeException();
        }
        return schoolClassRepository.save(SchoolClass.builder()
                .schoolClassName(schoolClassNameDto.getPlainText())
                .build());
    }

    public Student createStudent(StudentDto studentDto) {
        SchoolClass schoolClass = getSchoolClassByName(studentDto.getSchoolClassName()).orElseThrow();
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
        Teacher teacher = getTeacherById(teacherInClassDto.getTeacherId())
                .orElseThrow();
        SchoolSubject schoolSubject = getSchoolSubjectByName(teacherInClassDto.getTaughtSubject())
                .orElseThrow();
        SchoolClass schoolClass = getSchoolClassByName(teacherInClassDto.getSchoolClassName())
                .orElseThrow();
        if (!doesTeacherTeachTheSubject(teacher, schoolSubject)) {
            throw new RuntimeException();
        }
        return teacherInClassRepository.save(createTeacherInClass(teacher, schoolSubject, schoolClass));
    }

    public SchoolSubject addSchoolSubject(NameDto subjectNameDto) {
        if (doesSubjectAlreadyExistsInDatabase(subjectNameDto.getPlainText())) {
            throw new RuntimeException();
        }
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setSubjectName(subjectNameDto.getPlainText());
        return schoolSubjectRepository.save(schoolSubject);
    }

    private TeacherInClass createTeacherInClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        TeacherInClass teacherInClass = getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElse(new TeacherInClass());
        teacherInClass.setTeacher(teacher);
        teacherInClass.setTaughtSubject(schoolSubject);
        teacherInClass.getTaughtClasses().add(schoolClass);
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
                .map(subject -> getSchoolSubjectByName(subject).orElse(null))
                .collect(Collectors.toSet());
    }

    private boolean doesTeacherTeachTheSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }

    private Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher, SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private boolean doesSubjectAlreadyExistsInDatabase(String subjectName) {
        return schoolSubjectRepository.existsBySubjectName(subjectName);
    }

    private boolean doesSchoolClassAlreadyExistsInDatabase(String schoolClassName) {
        return schoolClassRepository.existsBySchoolClassName(schoolClassName);
    }

    private Optional<SchoolClass> getSchoolClassByName(String schoolClassName) {
        return schoolClassRepository.findBySchoolClassName(schoolClassName);
    }

    private Optional<SchoolSubject> getSchoolSubjectByName(String schoolSubjectName) {
        return schoolSubjectRepository.findBySubjectName(schoolSubjectName);
    }

    private Optional<Teacher> getTeacherById(int id) {
        return teacherRepository.findById(id);
    }
}
