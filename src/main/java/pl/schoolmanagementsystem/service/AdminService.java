package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.repository.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    private final TeacherRepository teacherRepository;

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;


















}
