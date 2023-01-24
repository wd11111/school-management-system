package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.repository.MarkRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.student.dto.MarkAvgDto;
import pl.schoolmanagementsystem.student.dto.MarkDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static pl.schoolmanagementsystem.student.utils.MarkMapper.mapListOfMarkDtoToDecimalsInMapStructure;

@Service
@RequiredArgsConstructor
public class StudentProfileService {

    private final SchoolSubjectRepository subjectRepository;

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;

    public Map<String, List<BigDecimal>> getGroupedMarksBySubject(String studentEmail) {
        List<MarkDto> studentsMarks = markRepository.findAllMarksForStudent(studentEmail);

        Map<String, List<MarkDto>> groupedUpMarks = groupMarksBySubject(studentsMarks);
        return mapListOfMarkDtoToDecimalsInMapStructure(groupedUpMarks);
    }

    public List<TaughtSubjectDto> getTaughtSubjectsInClass(String studentEmail) {
        String studentClass = studentRepository.findStudentsClass(studentEmail);
        return subjectRepository.findTaughtSubjectsInClass(studentClass);
    }

    public List<MarkAvgDto> getAverageMarks(String studentEmail) {
        return markRepository.findAllAveragesForStudent(studentEmail);
    }

    private Map<String, List<MarkDto>> groupMarksBySubject(List<MarkDto> studentsMarks) {
        return studentsMarks.stream()
                .collect(groupingBy(MarkDto::getSubject));
    }
}
