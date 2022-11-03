package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.dto.TextDto;
import pl.schoolmanagementsystem.repository.*;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    public SchoolClass createSchoolClass(TextDto schoolClassName) {
        if (doesSchoolClassAlreadyExistInDatabase(schoolClassName.getPlainText())) {
            throw new RuntimeException();
        }
        return schoolClassRepository.save(SchoolClass.builder()
                .schoolClassName(schoolClassName.getPlainText())
                .build());
    }

    private boolean doesSchoolClassAlreadyExistInDatabase(String schoolClassName) {
        return schoolClassRepository.existsBySchoolClassName(schoolClassName);
    }

}
