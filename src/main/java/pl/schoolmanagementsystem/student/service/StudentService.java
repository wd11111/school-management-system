package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.exception.NoSuchStudentEmailException;
import pl.schoolmanagementsystem.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.student.utils.StudentMapper;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.student.model.Student;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;
import pl.schoolmanagementsystem.schoolclass.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.student.repository.StudentRepository;
import pl.schoolmanagementsystem.student.utils.StudentBuilder;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        checkIfStudentExists(studentId);
        return groupMarksBySubjectForStudent(studentId);
    }

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudentAccount(String studentEmail) {
        Student student = findStudentByEmail(studentEmail);
        return groupMarksBySubjectForStudent(student.getId());
    }

    public List<MarkAvgDto> getAverageMarksForStudent(int studentId) {
        checkIfStudentExists(studentId);
        return findAllAverageMarksForStudentById(studentId);
    }

    public List<MarkAvgDto> getAveragesForStudentAccount(String email) {
        Student student = findStudentByEmail(email);
        return findAllAverageMarksForStudentById(student.getId());
    }

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

    private List<MarkAvgDto> findAllAverageMarksForStudentById(int id) {
        return studentRepository.findAllAverageMarksForStudentById(id);
    }

    public Map<String, List<Integer>> groupMarksBySubjectForStudent(int studentId) {
        List<MarkDtoWithTwoFields> studentsMarks = findAllMarksForStudent(studentId);
        Map<String, List<MarkDtoWithTwoFields>> groupedMarks = groupMarksBySubject(studentsMarks);
        return transformListOfMarksToListOfIntegers(groupedMarks);
    }

    public Student findById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchStudentException(id));
    }
    public Student findStudentByEmail(String email) {
        return studentRepository.findByEmail_Email(email)
                .orElseThrow(() -> new NoSuchStudentEmailException(email));
    }

    private void checkIfEmailIsAvailable(StudentInputDto studentInputDto) {
        emailService.checkIfEmailIsAvailable(studentInputDto.getEmail());
    }

    private void checkIfStudentExists(int studentId) {
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

    private List<MarkDtoWithTwoFields> findAllMarksForStudent(int id) {
        return studentRepository.findAllMarksForStudentById(id);
    }

    private Map<String, List<MarkDtoWithTwoFields>> groupMarksBySubject(List<MarkDtoWithTwoFields> studentsMarks) {
        return studentsMarks.stream()
                .collect(Collectors.groupingBy(MarkDtoWithTwoFields::getSubject));
    }

    private Map<String, List<Integer>> transformListOfMarksToListOfIntegers(Map<String, List<MarkDtoWithTwoFields>> mapToTransform) {
        Map<String, List<Integer>> resultMap = new HashMap<>();
        mapToTransform.forEach((key, value) -> resultMap.put(key, value
                .stream()
                .map(MarkDtoWithTwoFields::getMark)
                .collect(Collectors.toList())));
        return resultMap;
    }

}
