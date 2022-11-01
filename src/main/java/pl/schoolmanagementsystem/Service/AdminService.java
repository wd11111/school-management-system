package pl.schoolmanagementsystem.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.Repository.MarkRepository;
import pl.schoolmanagementsystem.Repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;


}
