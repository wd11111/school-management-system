package pl.schoolmanagementsystem.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.Model.Mark;
import pl.schoolmanagementsystem.Repository.MarkRepository;
import pl.schoolmanagementsystem.Repository.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;

    public Map<String, List<Integer>> getGroupedMarksBySubjectForStudent(int studentId) {
        List<Mark> studentsMarks = markRepository.findAllMarksForStudentById(studentId);
        Map<String, List<Mark>> groupedMarks = groupMarksBySubject(studentsMarks);
        return refactorListOfMarksToListOfIntegers(groupedMarks);
    }

    public List<MarkRepository.MarkAvg> getAllAverageMarksForStudentById(int studentId) {
        return markRepository.findAllAverageMarksForStudentById(studentId);
    }

    private Map<String, List<Mark>> groupMarksBySubject(List<Mark> studentsMarks) {
        return studentsMarks.stream().collect(Collectors.groupingBy(Mark::getSubject));
    }

    private Map<String, List<Integer>> refactorListOfMarksToListOfIntegers(Map<String, List<Mark>> mapToRefactor) {
        Map<String, List<Integer>> resultMap = new HashMap<>();
        mapToRefactor.forEach((key, value) -> resultMap.put(key, value
                .stream()
                .map(Mark::getMark)
                .collect(Collectors.toList())));
        return resultMap;
    }
}
