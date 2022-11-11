package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.schoolclass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.student.exception.NoSuchStudentEmailException;
import pl.schoolmanagementsystem.student.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.student.repository.StudentRepository;
import pl.schoolmanagementsystem.student.utils.StudentMapper;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.service.TeacherService;
import pl.schoolmanagementsystem.teacherinclass.service.TeacherInClassService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final EmailService emailService;

    private final StudentMapper studentMapper;

    private final SchoolSubjectService schoolSubjectService;

    private final TeacherService teacherService;

    private final SchoolClassService schoolClassService;

    private TeacherInClassService teacherInClassService;

    public StudentOutputDto createStudent(StudentInputDto studentInputDto) {
        emailService.checkIfEmailIsAvailable(studentInputDto.getEmail());
        SchoolClass schoolClass = findSchoolClass(studentInputDto);
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

    private SchoolClass findSchoolClass(StudentInputDto studentInputDto) {
        return schoolClassRepository.findBySchoolClassName(studentInputDto.getSchoolClassName())
                .orElseThrow(() -> new NoSuchSchoolClassException(studentInputDto.getSchoolClassName()));
    }
}
