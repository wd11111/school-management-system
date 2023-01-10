package pl.schoolmanagementsystem.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.common.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.student.service.StudentProfileService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static pl.schoolmanagementsystem.student.utils.MarkMapper.mapToListOfDoublesinMapStructure;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @GetMapping("/marks")
    public Map<String, List<Double>> getMarksGroupedBySubject(Principal principal) {
        return mapToListOfDoublesinMapStructure(studentProfileService.getGroupedMarksBySubject(principal.getName()));
    }

    @GetMapping("/averages")
    public List<MarkAvgDto> getAverageMarks(Principal principal) {
        return studentProfileService.getAverageMarks(principal.getName());
    }
}
