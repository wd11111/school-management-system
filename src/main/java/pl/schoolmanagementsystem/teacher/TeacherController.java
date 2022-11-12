package pl.schoolmanagementsystem.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndClassOutputDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherOutputDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherFacade teacherFacade;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<TeacherOutputDto>> getAllTeachersInSchool() {
        return ResponseEntity.ok(teacherFacade.getAllTeachersInSchool());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}/classes")
    public ResponseEntity<List<SubjectAndClassOutputDto>> getTaughtClassesByTeacher(@PathVariable int id) {
        return ResponseEntity.ok(teacherFacade.getTaughtClassesByTeacher(id));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<TeacherOutputDto> createTeacher(@RequestBody TeacherInputDto teacherInputDto) {
        return new ResponseEntity<>(teacherFacade.createTeacher(teacherInputDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}/subjects")
    public ResponseEntity<TeacherOutputDto> addTaughtSubjectToTeacher(
            @PathVariable int id, @RequestBody SchoolSubjectDto schoolSubjectDto) {
        return ResponseEntity.ok(teacherFacade.addTaughtSubjectToTeacher(id, schoolSubjectDto));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id) {
        teacherFacade.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
