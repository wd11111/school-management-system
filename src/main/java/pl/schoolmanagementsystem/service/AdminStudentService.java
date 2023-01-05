package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.mapper.StudentCreator;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.model.dto.StudentRequestDto;
import pl.schoolmanagementsystem.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.repository.AppUserRepository;
import pl.schoolmanagementsystem.exception.EmailAlreadyInUseException;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;


    private final EmailService emailService;

    private final AppUserRepository userRepository;

    private final StudentCreator studentCreator;

    @Transactional
    public Student createStudent(StudentRequestDto studentRequestDto) {
        validateEmailIsAvailable(studentRequestDto.getEmail());
        SchoolClass schoolClass = schoolClassRepository.findById(studentRequestDto.getSchoolClassName())
                .orElseThrow(() -> new NoSuchSchoolClassException(studentRequestDto.getSchoolClassName()));
        Student student = studentRepository.save(studentCreator.createStudent(studentRequestDto, schoolClass));
        emailService.sendEmail(studentRequestDto.getEmail(), student.getAppUser().getToken());
        return student;
    }

    public void deleteStudent(long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new NoSuchStudentException(studentId);
        }
        studentRepository.deleteById(studentId);
    }

    private void validateEmailIsAvailable(String email) {
        if (userRepository.existsById(email)) {
            throw new EmailAlreadyInUseException(email);
        }
    }
}
