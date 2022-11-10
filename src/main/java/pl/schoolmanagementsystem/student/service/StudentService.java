package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.exception.NoSuchStudentEmailException;
import pl.schoolmanagementsystem.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.student.repository.StudentRepository;
import pl.schoolmanagementsystem.student.utils.StudentBuilder;
import pl.schoolmanagementsystem.student.utils.StudentMapper;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final SchoolSubjectService schoolSubjectService;

    private final TeacherService teacherService;

    private final SchoolClassService schoolClassService;

    public StudentOutputDto createStudent(StudentInputDto studentInputDto) {
        checkIfEmailIsAvailable(studentInputDto);
        SchoolClass schoolClass = findSchoolClass(studentInputDto);
        Student student = studentRepository.save(buildStudent(studentInputDto, schoolClass));
        return StudentMapper.mapStudentToOutputDto(student);
    }

    public List<StudentOutputDto3> getAllStudentsInClassWithMarksOfTheSubject(String schoolClassName, String subjectName, String teacherEmail) {
        SchoolSubject schoolSubject = schoolSubjectService.findByName(subjectName);
        SchoolClass schoolClass = schoolClassService.find(schoolClassName);
        Teacher teacher = teacherService.findByEmail(teacherEmail);
        teacherService.makeSureIfTeacherTeachesThisClass(teacher, schoolSubject, schoolClass);
        return getStudentsWithIntegerMarks(schoolClassName, subjectName);
    }

    private List<StudentOutputDto3> getStudentsWithIntegerMarks(String schoolClassName, String subjectName) {
        List<Student> students = studentRepository.findAllStudentsInClassWithMarksOfTheSubject(schoolClassName, subjectName);
        return students.stream()
                .map(StudentMapper::mapStudentToOutputDto3)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStudent(int studentId) {
        checkIfStudentExists(studentId);
        studentRepository.deleteById(studentId);
    }

    public Student findById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchStudentException(id));
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail_Email(email)
                .orElseThrow(() -> new NoSuchStudentEmailException(email));
    }

    public int getIdFromPrincipals(Principal principal) {
        return findByEmail(principal.getName()).getId();
    }

    private void checkIfEmailIsAvailable(StudentInputDto studentInputDto) {
        emailService.checkIfEmailIsAvailable(studentInputDto.getEmail());
    }

    public void checkIfStudentExists(int studentId) {
        boolean doesStudentExist = studentRepository.existsById(studentId);
        if (!doesStudentExist) {
            throw new NoSuchStudentException(studentId);
        }
    }

    private Student buildStudent(StudentInputDto studentInputDto, SchoolClass schoolClass) {
        return StudentBuilder.buildStudent(studentInputDto, schoolClass, passwordEncoder);
    }

    private SchoolClass findSchoolClass(StudentInputDto studentInputDto) {
        return schoolClassRepository.findBySchoolClassName(studentInputDto.getSchoolClassName())
                .orElseThrow(() -> new NoSuchSchoolClassException(studentInputDto.getSchoolClassName()));
    }
}
