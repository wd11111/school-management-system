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

    private final TeacherInClassRepository teacherInClassRepository;

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public void addMark(MarkDto markDto) {
/*        Student student = getStudentById(markDto.getStudentId()).orElseThrow();

        Teacher teacher = teacherRepository.findById(markDto.getTeacherId()).orElseThrow();

        SchoolSubject schoolSubject = schoolSubjectRepository.findBySubjectName(markDto.getSubject()).orElseThrow();

        TeacherInClass teacherInClass = teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, ).orElseThrow();

        SchoolClass schoolClass = student.getSchoolClass();
        schoolClass.getTeachersInClass().contains()
        student.getSchoolClass().getTeachersInClass().contains()
        teacherInClassRepository.*/
    }

    private Optional<Student> getStudentById(int studentId) {
        return studentRepository.findById(studentId);
    }

}
