package pl.schoolmanagementsystem.admin.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.admin.common.mail.EmailSender;
import pl.schoolmanagementsystem.admin.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.admin.student.mapper.StudentCreator;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.schoolClass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.student.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.user.AppUserRepository;
import pl.schoolmanagementsystem.common.user.exception.EmailAlreadyInUseException;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final EmailSender emailSender;

    private final AppUserRepository userRepository;

    private final StudentCreator studentCreator;

    @Transactional
    public Student createStudent(CreateStudentDto createStudentDto) {
        validateEmailIsAvailable(createStudentDto.getEmail());

        SchoolClass schoolClass = schoolClassRepository.findById(createStudentDto.getSchoolClassName())
                .orElseThrow(() -> new NoSuchSchoolClassException(createStudentDto.getSchoolClassName()));

        Student student = studentRepository.save(studentCreator.createStudent(createStudentDto, schoolClass));
        emailSender.sendEmail(createStudentDto.getEmail(), student.getAppUser().getToken());
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
