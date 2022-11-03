package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schoolmanagementsystem.model.Mark;
import pl.schoolmanagementsystem.model.dto.MarkDto;
import pl.schoolmanagementsystem.service.TeacherService;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/classes/{schoolClassName}/marks")
    public ResponseEntity<Mark> addMark(@PathVariable int schoolClassName, @RequestBody MarkDto markDto) {
        return new ResponseEntity<>(teacherService.addMark(markDto), HttpStatus.CREATED);
    }
}
