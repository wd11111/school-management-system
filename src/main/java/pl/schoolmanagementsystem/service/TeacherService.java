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

    private final SchoolClassRepository schoolClassRepository;

    public Mark addMark(MarkDto markDto) {
        Student student = findStudent(markDto.getStudentId());
        SchoolClass studentsClass = student.getSchoolClass();
        Teacher teacher = findTeacher(markDto.getTeacherId());
        SchoolSubject schoolSubject = findSubject(markDto.getSubject());
        checkIfTeacherTeachesThisClass(teacher, schoolSubject, studentsClass);
        return markRepository.save(createMark(markDto.getMark(), student, schoolSubject));
    }

    public List<SubjectClassDto> showTaughtClassesByTeacher(int teacherId) {
        return teacherInClassRepository.findTaughtClassesByTeacher(teacherId);
    }

    public Teacher createTeacher(TeacherDto teacherDto) {
        return teacherRepository.save(Teacher.builder()
                .name(teacherDto.getName())
                .surname(teacherDto.getSurname())
                .teacherInClasses(new HashSet<>())
                .build());
    }

    private Mark createMark(int mark, Student student, SchoolSubject schoolSubject) {
        return Mark.builder()
                .mark(mark)
                .student(student)
                .subject(schoolSubject)
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
