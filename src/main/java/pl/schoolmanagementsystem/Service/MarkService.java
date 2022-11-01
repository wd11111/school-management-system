package pl.schoolmanagementsystem.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.Model.Mark;
import pl.schoolmanagementsystem.Repository.MarkRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;

    public List<Mark> getAllMarksForStudentById(int studentId) {
        return markRepository.findAllMarksForStudentById(studentId);
    }

    public List<MarkRepository.MarkAvg> getAllAverageMarksForStudentById(int studentId) {
        return markRepository.findAllAverageMarksForStudentById(studentId);
    }
}
