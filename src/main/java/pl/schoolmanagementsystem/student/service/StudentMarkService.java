package pl.schoolmanagementsystem.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.mark.MarkRepository;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;
import pl.schoolmanagementsystem.common.student.StudentRepository;

import java.util.List;
import java.util.Map;

import static pl.schoolmanagementsystem.common.mark.MarkMapper.groupMarksBySubject;

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
}
