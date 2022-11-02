package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.dto.MarkAvg;
import pl.schoolmanagementsystem.repository.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final MarkService markService;

    private final StudentRepository studentRepository;

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        List<Mark> studentsMarks = markService.getAllMarksForStudentById(studentId);
        Map<String, List<Mark>> groupedMarks = groupMarksBySubject(studentsMarks);
        return transformListOfMarksToListOfIntegers(groupedMarks);
    }

    public List<MarkAvg> getAllAverageMarksForStudentById(int studentId) {
        return studentRepository.findAllAverageMarksForStudentById(studentId);
    }

    private Map<String, List<Mark>> groupMarksBySubject(List<Mark> studentsMarks) {
        return studentsMarks.stream()
                .collect(Collectors.groupingBy(Mark::getSubject));
    }

    private Map<String, List<Integer>> transformListOfMarksToListOfIntegers(Map<String, List<Mark>> mapToRefactor) {
        Map<String, List<Integer>> resultMap = new HashMap<>();
        mapToRefactor.forEach((key, value) -> resultMap.put(key, value
                .stream()
                .map(Mark::getMark)
                .collect(Collectors.toList())));
        return resultMap;
    }
}
