package pl.schoolmanagementsystem.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.mark.MarkFacade;
import pl.schoolmanagementsystem.mark.dto.MarkAvgDto;
import pl.schoolmanagementsystem.mark.dto.MarkInputDto;
import pl.schoolmanagementsystem.mark.dto.MarkOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentFacade studentFacade;

    private final MarkFacade markFacade;

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}/marks")
    public ResponseEntity<Map<String, List<Integer>>> getGroupedMarksBySubjectForStudent(@PathVariable int id) {
        return ResponseEntity.ok(markFacade.getGroupedMarksBySubjectForStudent(id));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}/averages")
    public ResponseEntity<List<MarkAvgDto>> getAverageMarksForStudent(@PathVariable int id) {
        return ResponseEntity.ok(markFacade.getAverageMarksForStudent(id));
    }

    @Secured("ROLE_TEACHER")
    @PostMapping("/{id}/marks")
    public ResponseEntity<MarkOutputDto> addMark(@PathVariable int id, @RequestBody MarkInputDto markInputDto,
                                                 Principal principal) {
        return new ResponseEntity<>(markFacade.addMarkForStudent(principal.getName(), markInputDto, id), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<StudentOutputDto> createStudent(@RequestBody StudentInputDto studentInputDto) {
        return new ResponseEntity<>(studentFacade.createStudent(studentInputDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        studentFacade.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
