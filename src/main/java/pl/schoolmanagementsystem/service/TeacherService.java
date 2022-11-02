package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.repository.MarkRepository;
import pl.schoolmanagementsystem.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;


}
