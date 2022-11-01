package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.Service.StudentService;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class AdminController {

    private final StudentService studentService;

}
