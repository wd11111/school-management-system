package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        Student student = studentRepository.findById(markDto.getStudentId())
                .orElseThrow();
        SchoolClass studentsClass = student.getSchoolClass();
        Teacher teacher = teacherRepository.findById(markDto.getTeacherId())
                .orElseThrow();
        SchoolSubject schoolSubject = schoolSubjectRepository.findBySubjectName(markDto.getSubject())
                .orElseThrow();
        if (doesTeacherTeachThisClass(teacher, schoolSubject, studentsClass)) {
            Mark mark = new Mark();
            mark.setMark(markDto.getMark());
            mark.setStudent(student);
            mark.setSubject(schoolSubject.getSubjectName());
            return markRepository.save(mark);
        }
        throw new RuntimeException();
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

    private boolean doesTeacherTeachThisClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        return getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElseThrow()
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
