package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.mark.MarkMapper;
import pl.schoolmanagementsystem.common.mark.MarkRepository;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkDtoWithTwoFields;
import pl.schoolmanagementsystem.common.student.StudentRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentMarkService {

    private final StudentRepository studentRepository;

    private final MarkRepository markRepository;

    public Map<String, List<MarkDtoWithTwoFields>> getGroupedMarksBySubject(String studentEmail) {
        List<MarkDtoWithTwoFields> studentsMarks = markRepository.findAllMarksForStudent(studentEmail);
        return MarkMapper.groupMarksBySubject(studentsMarks);
    }

    public List<MarkAvgDto> getAverageMarks(String studentEmail) {
        return markRepository.findAllAveragesForStudent(getStudentId(studentEmail));
    }

    private int getStudentId(String studentEmail) {
        return studentRepository.findIdByEmail(studentEmail);
    }
}
