package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.exception.EmailAlreadyInUseException;
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
import pl.schoolmanagementsystem.repository.EmailRepository;
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

    private final EmailRepository emailRepository;

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        checkIfStudentExists(studentId);
        List<MarkDtoWithTwoFields> studentsMarks = findAllMarksForStudent(studentId);
        Map<String, List<MarkDtoWithTwoFields>> groupedMarks = groupMarksBySubject(studentsMarks);
        return transformListOfMarksToListOfIntegers(groupedMarks);
    }

    public List<MarkAvgDto> getAverageMarksForStudent(int studentId) {
        checkIfStudentExists(studentId);
        return studentRepository.findAllAverageMarksForStudentById(studentId);
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

    private boolean isEmailAvailable(String email) {
        return emailRepository.existsById(email);
    }

    private void checkIfEmailIsAvailable(StudentInputDto studentInputDto) {
        if (isEmailAvailable(studentInputDto.getEmail())) {
            throw new EmailAlreadyInUseException(studentInputDto.getEmail());
        }
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
                .schoolClass(schoolClass)
                .email(new Email(studentInputDto.getEmail()))
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
