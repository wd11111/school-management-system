package pl.schoolmanagementsystem.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.Model.SchoolClass;
import pl.schoolmanagementsystem.Model.Student;
import pl.schoolmanagementsystem.Model.dto.CreateStudentDto;
import pl.schoolmanagementsystem.Repository.MarkRepository;
import pl.schoolmanagementsystem.Repository.SchoolClassRepository;
import pl.schoolmanagementsystem.Repository.StudentRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MarkRepository markRepository;

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;

    public SchoolClass createSchoolClass(String name) {
        return schoolClassRepository.save(new SchoolClass(0, name, Collections.emptyList()));
    }

    public Student createStudent(CreateStudentDto createStudentDto) {
        SchoolClass schoolClass = schoolClassRepository.findByName(createStudentDto.getSchoolClassName())
                .orElseThrow();
        return new Student(0,
                createStudentDto.getName(),
                createStudentDto.getSurname(),
                Collections.emptyList(),
                schoolClass);
    }

}
