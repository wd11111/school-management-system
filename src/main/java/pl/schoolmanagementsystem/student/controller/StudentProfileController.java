package pl.schoolmanagementsystem.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.common.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.student.dto.MarkAvgDto;
import pl.schoolmanagementsystem.student.service.StudentProfileService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @GetMapping("/marks")
    public Map<String, List<BigDecimal>> getMarksGroupedBySubject(Principal principal) {
        return studentProfileService.getGroupedMarksBySubject(principal.getName());
    }

    @GetMapping("/taught-subjects")
    public List<TaughtSubjectDto> getTaughtSubjectsInClass(Principal principal) {
        return studentProfileService.getTaughtSubjectsInClass(principal.getName());
    }

    @GetMapping("/averages")
    public List<MarkAvgDto> getAverageMarks(Principal principal) {
        return studentProfileService.getAverageMarks(principal.getName());
    }
}
