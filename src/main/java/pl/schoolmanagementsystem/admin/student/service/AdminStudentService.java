package pl.schoolmanagementsystem.admin.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.admin.mailSender.MailSenderService;
import pl.schoolmanagementsystem.admin.student.mapper.StudentCreator;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.schoolClass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.common.student.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.user.AppUserRepository;
import pl.schoolmanagementsystem.common.user.exception.EmailAlreadyInUseException;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;


    private final MailSenderService mailSenderService;

    private final AppUserRepository userRepository;

    private final StudentCreator studentCreator;

    @Transactional
    public Student createStudent(StudentInputDto studentInputDto) {
        checkIfEmailIsAvailable(studentInputDto.getEmail());
        SchoolClass schoolClass = schoolClassRepository.findById(studentInputDto.getSchoolClassName())
                .orElseThrow(() -> new NoSuchSchoolClassException(studentInputDto.getSchoolClassName()));
        Student student = studentRepository.save(studentCreator.createStudent(studentInputDto, schoolClass));
        mailSenderService.sendEmail(studentInputDto.getEmail(), student.getAppUser().getToken());
        return student;
    }

    public void deleteStudent(int studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new NoSuchStudentException(studentId);
        }
        studentRepository.deleteById(studentId);
    }

    private void checkIfEmailIsAvailable(String email) {
        if (userRepository.existsById(email)) {
            throw new EmailAlreadyInUseException(email);
        }
    }
}
