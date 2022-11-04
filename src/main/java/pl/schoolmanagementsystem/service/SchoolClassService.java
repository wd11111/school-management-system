package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.dto.TextDto;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    public SchoolClass createSchoolClass(TextDto schoolClassName) {
        return schoolClassRepository.save(buildSchoolClass(schoolClassName));
    }

    private SchoolClass buildSchoolClass(TextDto schoolClassName) {
        return SchoolClass.builder()
                .schoolClassName(schoolClassName.getPlainText())
                .build();
    }
}
