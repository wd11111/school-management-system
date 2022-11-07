package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.mapper.StudentMapper;
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

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        List<MarkDtoWithTwoFields> studentsMarks = findAllMarksForStudent(studentId);
        Map<String, List<MarkDtoWithTwoFields>> groupedMarks = groupMarksBySubject(studentsMarks);
        return transformListOfMarksToListOfIntegers(groupedMarks);
    }

    public List<MarkAvgDto> getAverageMarksForStudent(int studentId) {
        return studentRepository.findAllAverageMarksForStudentById(studentId);
    }

    public StudentOutputDto createStudent(StudentInputDto studentInputDto) {
        SchoolClass schoolClass = findSchoolClass(studentInputDto.getSchoolClassName());
        Student student = studentRepository.save(buildStudent(studentInputDto, schoolClass));
        return StudentMapper.mapStudentToOutputDto(student);
    }

    private List<MarkDtoWithTwoFields> findAllMarksForStudent(int id) {
        return studentRepository.findAllMarksForStudentById(id);
    }

    private Student buildStudent(StudentInputDto studentInputDto, SchoolClass schoolClass) {
        return Student.builder()
                .name(studentInputDto.getName())
                .surname(studentInputDto.getSurname())
                .schoolClass(schoolClass)
                .build();
    }

    private SchoolClass findSchoolClass(String name) {
        return schoolClassRepository.findBySchoolClassName(name)
                .orElseThrow(() -> new NoSuchSchoolClassException(name));
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
