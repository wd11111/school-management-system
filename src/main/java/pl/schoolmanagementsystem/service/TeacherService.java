package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.AddMarkDto;
import pl.schoolmanagementsystem.repository.MarkRepository;
import pl.schoolmanagementsystem.repository.StudentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;

    public void addMark(AddMarkDto addMarkDto) {
        Student student = getStudentById(addMarkDto.getStudentId()).orElseThrow();
    }

    private Optional<Student> getStudentById(int studentId) {
        return studentRepository.findById(studentId);
    }

}
