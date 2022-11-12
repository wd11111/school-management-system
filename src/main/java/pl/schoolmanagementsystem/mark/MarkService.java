package pl.schoolmanagementsystem.mark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.dto.MarkDtoWithTwoFields;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;

    public Mark save(Mark mark) {
        return markRepository.save(mark);
    }

    public List<MarkAvgDto> findAllAverageMarksForStudentById(int studentId) {
        return markRepository.findAllAverageMarksForStudentById(studentId);
    }

    public List<MarkDtoWithTwoFields> findAllMarksForStudent(int studentId) {
        return markRepository.findAllMarksForStudentById(studentId);
    }

    public Map<String, List<MarkDtoWithTwoFields>> groupMarksBySubject(List<MarkDtoWithTwoFields> studentsMarks) {
        return studentsMarks.stream()
                .collect(Collectors.groupingBy(MarkDtoWithTwoFields::getSubject));
    }
}
