package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.exception.NoSuchStudentException;
import pl.schoolmanagementsystem.mapper.StudentMapper;
import pl.schoolmanagementsystem.model.Email;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.model.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.model.dto.input.StudentInputDto;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.repository.StudentRepository;

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

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        checkIfStudentExists(studentId);
        return groupMarksBySubjectForStudent(studentId);
    }

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudentAccount(String studentEmail) {
        Student student = findStudent(studentEmail);
        return groupMarksBySubjectForStudent(student.getStudentId());
    }

    public List<MarkAvgDto> getAverageMarksForStudent(int studentId) {
        checkIfStudentExists(studentId);
        return findAllAverageMarksForStudentById(studentId);
    }

    public List<MarkAvgDto> getAveragesForStudentAccount(String email) {
        Student student = findStudent(email);
        return findAllAverageMarksForStudentById(student.getStudentId());
    }

    public StudentOutputDto createStudent(StudentInputDto studentInputDto) {
        checkIfEmailIsAvailable(studentInputDto);
        SchoolClass schoolClass = findSchoolClass(studentInputDto);
        Student student = studentRepository.save(buildStudent(studentInputDto, schoolClass));
        return StudentMapper.mapStudentToOutputDto(student);
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

    private Student findStudent(String email) {
        return studentRepository.findByEmail_Email(email)
                .orElseThrow(() -> new NoSuchStudentException(1337));
    }

    private void checkIfEmailIsAvailable(StudentInputDto studentInputDto) {
        emailService.checkIfEmailIsAvailable(studentInputDto.getEmail());
    }

    private void checkIfStudentExists(int studentId) {
        if (!doesStudentExist(studentId)) {
            throw new NoSuchStudentException(studentId);
        }
    }

    private boolean doesStudentExist(int studentId) {
        return studentRepository.existsById(studentId);
    }

    private Student buildStudent(StudentInputDto studentInputDto, SchoolClass schoolClass) {
        return Student.builder()
                .name(studentInputDto.getName())
                .surname(studentInputDto.getSurname())
                .email(new Email(studentInputDto.getEmail()))
                .password(passwordEncoder.encode(studentInputDto.getPassword()))
                .schoolClass(schoolClass)
                .build();
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
