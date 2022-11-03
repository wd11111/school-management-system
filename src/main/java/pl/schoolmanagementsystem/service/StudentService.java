package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.Student;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.model.dto.StudentDto;
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
        List<Mark> studentsMarks = studentRepository.findAllMarksForStudentById(studentId);
        Map<String, List<Mark>> groupedMarks = groupMarksBySubject(studentsMarks);
        return transformListOfMarksToListOfIntegers(groupedMarks);
    }

    public List<MarkAvgDto> getAllAverageMarksForStudentById(int studentId) {
        return studentRepository.findAllAverageMarksForStudentById(studentId);
    }

    public Student createStudent(StudentDto studentDto) {
        SchoolClass schoolClass = schoolClassRepository.findBySchoolClassName(studentDto.getSchoolClassName())
                .orElseThrow();
        return studentRepository.save(Student.builder()
                .name(studentDto.getName())
                .surname(studentDto.getSurname())
                .schoolClass(schoolClass)
                .build());
    }

    private Map<String, List<Mark>> groupMarksBySubject(List<Mark> studentsMarks) {
        return studentsMarks.stream()
                .collect(Collectors.groupingBy(Mark::getSubject));
    }

    private Map<String, List<Integer>> transformListOfMarksToListOfIntegers(Map<String, List<Mark>> mapToTransform) {
        Map<String, List<Integer>> resultMap = new HashMap<>();
        mapToTransform.forEach((key, value) -> resultMap.put(key, value
                .stream()
                .map(Mark::getMark)
                .collect(Collectors.toList())));
        return resultMap;
    }

}
