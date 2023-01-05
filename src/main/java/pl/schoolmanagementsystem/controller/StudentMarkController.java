package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.service.StudentMarkService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static pl.schoolmanagementsystem.mapper.MarkMapper2.mapToListOfBytesInMapStructure;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentMarkController {

    private final StudentMarkService studentMarkService;

    @GetMapping("/marks")
    public Map<String, List<Byte>> getGroupedMarksBySubject(Principal principal) {
        return mapToListOfBytesInMapStructure(studentMarkService.getGroupedMarksBySubject(principal.getName()));
        // todo tu podobnie wyniósłbym ten mapping do servicu
    }

    @GetMapping("/averages")
    public List<MarkAvgDto> getAverageMarks(Principal principal) {
        return studentMarkService.getAverageMarks(principal.getName());
    }
}
