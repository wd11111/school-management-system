package pl.schoolmanagementsystem.admin.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.admin.email.EmailRepository;
import pl.schoolmanagementsystem.admin.schoolClass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.admin.student.mapper.StudentMapper;
import pl.schoolmanagementsystem.common.email.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.common.student.exception.NoSuchStudentException;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentRepository studentRepository;

    private final EmailRepository emailRepository;

    private final SchoolClassRepository schoolClassRepository;

    public Student createStudent(StudentInputDto studentInputDto) {
        checkIfEmailIsAvailable(studentInputDto.getEmail());
        SchoolClass schoolClass = schoolClassRepository.findById(studentInputDto.getSchoolClassName())
                .orElseThrow(() -> new NoSuchSchoolClassException(studentInputDto.getSchoolClassName()));
        return studentRepository.save(StudentMapper.mapToStudent(studentInputDto, schoolClass));
    }

    public void deleteStudent(int studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new NoSuchStudentException(studentId);
        }
        studentRepository.deleteById(studentId);
    }

    private void checkIfEmailIsAvailable(String email) {
        if (emailRepository.existsById(email)) {
            throw new EmailAlreadyInUseException(email);
        }
    }
}
