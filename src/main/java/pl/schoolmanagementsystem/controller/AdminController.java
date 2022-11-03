package pl.schoolmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.repository.TeacherRepository;
import pl.schoolmanagementsystem.service.AdminService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private final StudentRepository studentRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final TeacherRepository teacherRepository;

}
