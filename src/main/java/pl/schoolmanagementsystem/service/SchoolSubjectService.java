package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;

@Service
@RequiredArgsConstructor
public class SchoolSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolSubjectDto createSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        schoolSubjectRepository.save(buildSchoolSubject(schoolSubjectDto));
        return schoolSubjectDto;
    }

    private SchoolSubject buildSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .subjectName(schoolSubjectDto.getSubject())
                .build();
    }
}
