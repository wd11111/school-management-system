package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.model.dto.output.StudentOutputDto2;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    private final StudentRepository studentRepository;

    public SchoolClassDto createSchoolClass(SchoolClassDto schoolClassDto) {
        checkIfClassAlreadyExists(schoolClassDto);
        schoolClassRepository.save(buildSchoolClass(schoolClassDto));
        return schoolClassDto;
    }

    public List<StudentOutputDto2> getAllStudentsInClass(String schoolClassName) {
        checkIfClassExists(schoolClassName);
        return studentRepository.findAllStudentsInClass(schoolClassName);
    }

    private SchoolClass buildSchoolClass(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .schoolClassName(schoolClassDto.getSchoolClassName())
                .build();
    }

    private void checkIfClassExists(String schoolClassName) {
        if (!schoolClassExistsByName(schoolClassName)) {
         throw new NoSuchSchoolClassException(schoolClassName);
        }
    }

    private void checkIfClassAlreadyExists(SchoolClassDto schoolClassDto) {
        if (schoolClassExistsByName(schoolClassDto.getSchoolClassName())) {
            throw new ClassAlreadyExistsException(schoolClassDto);
        }
    }

    private boolean schoolClassExistsByName(String schoolClassName) {
        return schoolClassRepository.existsBySchoolClassName(schoolClassName);
    }
}
