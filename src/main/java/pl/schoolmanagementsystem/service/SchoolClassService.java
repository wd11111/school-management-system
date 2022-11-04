package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    public SchoolClassDto createSchoolClass(SchoolClassDto schoolClassDto) {
        checkIfClassAlreadyExists(schoolClassDto);
        schoolClassRepository.save(buildSchoolClass(schoolClassDto));
        return schoolClassDto;
    }

    private SchoolClass buildSchoolClass(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .schoolClassName(schoolClassDto.getSchoolClassName())
                .build();
    }

    private void checkIfClassAlreadyExists(SchoolClassDto schoolClassDto) {
        boolean existsBySchoolClassName = schoolClassRepository.existsBySchoolClassName(
                schoolClassDto.getSchoolClassName());
        if (existsBySchoolClassName) {
            throw new ClassAlreadyExistsException(schoolClassDto);
        }
    }
}
