package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.repository.MarkRepository;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.model.dto.MarkDto;
import pl.schoolmanagementsystem.repository.StudentRepository;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class StudentMarkService {

    private final StudentRepository studentRepository;

    private final MarkRepository markRepository;

    public Map<String, List<MarkDto>> getGroupedMarksBySubject(String studentEmail) {
        List<MarkDto> studentsMarks = markRepository.findAllMarksForStudent(studentEmail);
        return groupMarksBySubject(studentsMarks);
    }

    public List<MarkAvgDto> getAverageMarks(String studentEmail) {
        return markRepository.findAllAveragesForStudent(getStudentId(studentEmail));
    }

    private long getStudentId(String studentEmail) {
        return studentRepository.findIdByEmail(studentEmail);
    }

    private Map<String, List<MarkDto>> groupMarksBySubject(List<MarkDto> studentsMarks) {
        return studentsMarks.stream()
                .collect(groupingBy(MarkDto::getSubject));
    }
}
