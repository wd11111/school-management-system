package pl.schoolmanagementsystem.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.email.EmailService;
import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubjectService;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.student.exception.NoSuchStudentEmailException;
import pl.schoolmanagementsystem.student.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.student.utils.StudentMapper;
import pl.schoolmanagementsystem.teacher.Teacher;
import pl.schoolmanagementsystem.teacher.TeacherService;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClassService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    private final EmailService emailService;

    private final StudentMapper studentMapper;

    private final SchoolSubjectService schoolSubjectService;

    private final TeacherService teacherService;

    private final SchoolClassService schoolClassService;

    private final TeacherInClassService teacherInClassService;

    public StudentOutputDto createStudent(StudentInputDto studentInputDto) {
        emailService.checkIfEmailIsAvailable(studentInputDto.getEmail());
        SchoolClass schoolClass = schoolClassService.findById(studentInputDto.getName());
        Student student = studentRepository.save(studentMapper.mapInputDtoToStudent(studentInputDto, schoolClass));
        return studentMapper.mapStudentToOutputDto(student);
    }

    public List<StudentOutputDto3> getAllStudentsInClassWithMarksOfTheSubject(String schoolClassName, String subjectName, int teacherId) {
        SchoolSubject schoolSubject = schoolSubjectService.findByName(subjectName);
        SchoolClass schoolClass = schoolClassService.findById(schoolClassName);
        Teacher teacher = teacherService.findById(teacherId);
        teacherInClassService.makeSureIfTeacherTeachesThisClass(teacher, schoolSubject, schoolClass);
        return studentRepository.findAllStudentsInClassWithMarksOfTheSubject(schoolClassName, subjectName)
                .stream()
                .map(studentMapper::mapStudentToOutputDto3)
                .collect(Collectors.toList());
    }

    public List<StudentOutputDto2> getAllStudentsInClass(String schoolClassName) {
        schoolClassService.checkIfClassExists(schoolClassName);
        return studentRepository.findAllStudentsInClass(schoolClassName);
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

    public void checkIfStudentExists(int studentId) {
        boolean doesStudentExist = studentRepository.existsById(studentId);
        if (!doesStudentExist) {
            throw new NoSuchStudentException(studentId);
        }
    }
}
