package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.criteria.dto.SearchRequestDto;
import pl.schoolmanagementsystem.common.email.service.EmailSender;
import pl.schoolmanagementsystem.common.exception.EmailAlreadyInUseException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.common.role.AppUserService;
import pl.schoolmanagementsystem.common.role.RoleAdder;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentSearchDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;
import pl.schoolmanagementsystem.student.search.StudentSearcher;
import pl.schoolmanagementsystem.student.utils.StudentMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final EmailSender emailSender;

    private final AppUserRepository userRepository;

    private final RoleAdder roleAdder;

    private final StudentMapper studentMapper;

    private final StudentSearcher studentSearcher;

    @Transactional
    public StudentWithClassDto createStudent(CreateStudentDto dto) {
        validateEmailIsAvailable(dto.getEmail());
        validateClassExists(dto.getSchoolClass());

        AppUser appUser = AppUserService.createAppUser(dto.getEmail());
        Student student = studentMapper.mapCreateDtoToEntity(dto, appUser);
        roleAdder.addRoles(student);

        studentRepository.saveAndFlush(student);

        emailSender.sendEmail(dto.getEmail(), student.getAppUser().getToken());
        return studentMapper.mapEntityToDtoWithSchoolClass(student);
    }

    public List<StudentSearchDto> searchStudent(List<SearchRequestDto> searchRequestDtos) {
        List<Student> students = studentSearcher.searchStudent(searchRequestDtos);

        return studentMapper.mapEntitiesToSearchDtos(students);
    }

    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new NoSuchStudentException(studentId);
        }
        studentRepository.deleteById(studentId);
    }

    private void validateClassExists(String schoolClassName) {
        if (!schoolClassRepository.existsById(schoolClassName)) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
    }

    private void validateEmailIsAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(email);
        }
    }
}
