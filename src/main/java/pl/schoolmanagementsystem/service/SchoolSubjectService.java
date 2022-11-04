package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.dto.TextDto;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;

@Service
@RequiredArgsConstructor
public class SchoolSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolSubject createSchoolSubject(TextDto subjectName) {
        return schoolSubjectRepository.save(buildSchoolSubject(subjectName));
    }

    private SchoolSubject buildSchoolSubject(TextDto subjectName) {
        return SchoolSubject.builder()
                .subjectName(subjectName.getPlainText())
                .build();
    }
}
