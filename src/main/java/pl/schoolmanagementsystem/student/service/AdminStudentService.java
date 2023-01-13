package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.email.service.EmailSender;
import pl.schoolmanagementsystem.common.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.common.role.RoleAdder;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;

import static pl.schoolmanagementsystem.student.utils.StudentMapper.mapCreateDtoToEntity;
import static pl.schoolmanagementsystem.student.utils.StudentMapper.mapEntityToDtoWithSchoolClass;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final EmailSender emailSender;

    private final AppUserRepository userRepository;

    private final RoleAdder roleAdder;

    @Transactional
    public StudentWithClassDto createStudent(CreateStudentDto createStudentDto) {
        validateEmailIsAvailable(createStudentDto.getEmail());

        SchoolClass schoolClass = schoolClassRepository.findById(createStudentDto.getSchoolClassName())
                .orElseThrow(() -> new NoSuchSchoolClassException(createStudentDto.getSchoolClassName()));

        Student student = mapCreateDtoToEntity(createStudentDto, schoolClass);
        roleAdder.addRoles(student);
        Student savedStudent = studentRepository.save(student);
        emailSender.sendEmail(createStudentDto.getEmail(), student.getAppUser().getToken());
        return mapEntityToDtoWithSchoolClass(savedStudent);
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
