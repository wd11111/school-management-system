package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.criteria.FilterService;
import pl.schoolmanagementsystem.common.criteria.SearchRequestDto;
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
import pl.schoolmanagementsystem.common.utils.StudentMapper;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentSearchDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;

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

    private final FilterService<Student> filterService;

    @Transactional
    public StudentWithClassDto createStudent(CreateStudentDto createStudentDto) {
        validateEmailIsAvailable(createStudentDto.getEmail());
        validateClassExists(createStudentDto.getSchoolClass());

        AppUser appUser = AppUserService.createAppUser(createStudentDto.getEmail());
        Student student = studentMapper.mapCreateDtoToEntity(createStudentDto, appUser);
        roleAdder.addRoles(student);
        Student savedStudent = studentRepository.save(student);
        emailSender.sendEmail(createStudentDto.getEmail(), student.getAppUser().getToken());
        return studentMapper.mapEntityToDtoWithSchoolClass(savedStudent);
    }

    public List<StudentSearchDto> searchStudent(List<SearchRequestDto> searchRequestDtos) {
        Specification<Student> searchSpecification = filterService.getSearchSpecification(searchRequestDtos);
        searchSpecification.toString();
        List<Student> students = studentRepository.findAll(searchSpecification);
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
        if (userRepository.existsById(email)) {
            throw new EmailAlreadyInUseException(email);
        }
    }
}
