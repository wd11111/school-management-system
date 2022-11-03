package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.*;
import pl.schoolmanagementsystem.model.dto.MarkDto;
import pl.schoolmanagementsystem.repository.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final StudentRepository studentRepository;

    private final AdminService adminService;

    private final MarkRepository markRepository;


    private final TeacherInClassRepository teacherInClassRepository;

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public Mark addMark(MarkDto markDto) {
        Student student = getStudentById(markDto.getStudentId())
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

    private boolean doesTeacherTeachThisClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        return adminService.getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElseThrow()
                .getTaughtClasses()
                .contains(schoolClass);
    }

    private Optional<Student> getStudentById(int studentId) {
        return studentRepository.findById(studentId);
    }

}
