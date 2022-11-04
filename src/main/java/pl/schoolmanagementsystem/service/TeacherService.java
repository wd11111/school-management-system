package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.model.*;
import pl.schoolmanagementsystem.model.dto.MarkDto;
import pl.schoolmanagementsystem.model.dto.SubjectClassDto;
import pl.schoolmanagementsystem.model.dto.TeacherDto;
import pl.schoolmanagementsystem.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final StudentRepository studentRepository;

    private final MarkRepository markRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public Mark addMark(MarkDto markDto, int studentId) {
        Student student = findStudent(studentId);
        SchoolClass studentsClass = student.getSchoolClass();
        Teacher teacher = findTeacher(markDto.getTeacherId());
        SchoolSubject schoolSubject = findSubject(markDto.getSubject());
        checkIfTeacherTeachesThisClass(teacher, schoolSubject, studentsClass);
        return markRepository.save(buildMark(markDto.getMark(), student, schoolSubject));
    }

    public List<SubjectClassDto> getTaughtClassesByTeacher(int teacherId) {
        return teacherInClassRepository.findTaughtClassesByTeacher(teacherId);
    }

    public Teacher createTeacher(TeacherDto teacherDto) {
        return teacherRepository.save(buildTeacher(teacherDto));
    }

    public List<Teacher> getAllTeachersInSchool() {
        return teacherRepository.findAll();
    }

    private Mark buildMark(int mark, Student student, SchoolSubject schoolSubject) {
        return Mark.builder()
                .mark(mark)
                .student(student)
                .subject(schoolSubject)
                .build();
    }

    private Teacher buildTeacher(TeacherDto teacherDto) {
        return Teacher.builder()
                .name(teacherDto.getName())
                .surname(teacherDto.getSurname())
                .teacherInClasses(new HashSet<>())
                .build();
    }

    private SchoolSubject findSubject(String subjectName) {
        return schoolSubjectRepository.findBySubjectName(subjectName)
                .orElseThrow(() -> new NoSuchSchoolSubjectException(subjectName));
    }

    private Student findStudent(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchStudentException(id));
    }

    private Teacher findTeacher(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchTeacherException(id));
    }

    private boolean checkIfTeacherTeachesThisClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        return getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElseThrow(() -> new TeacherDoesNotTeachClassException(schoolSubject, schoolClass))
                .getTaughtClasses()
                .contains(schoolClass);
    }

    private Optional<TeacherInClass> getTeacherInClassIfTheTeacherAlreadyHasEquivalent(Teacher teacher,
                                                                                       SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    private Set<SchoolSubject> transformSetOfStringsToSetOfSchoolClassObjects(Set<String> subjects) {
        return subjects.stream()
                .filter(subject -> doesSubjectAlreadyExistInDatabase(subject))
                .map(subject -> schoolSubjectRepository.findBySubjectName(subject)
                        .orElse(null))
                .collect(Collectors.toSet());
    }

    private boolean doesSubjectAlreadyExistInDatabase(String subjectName) {
        return schoolSubjectRepository.existsBySubjectName(subjectName);
    }
}
