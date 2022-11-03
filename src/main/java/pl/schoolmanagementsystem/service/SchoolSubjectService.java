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

    public SchoolSubject addSchoolSubject(TextDto subjectName) {
        if (doesSubjectAlreadyExistInDatabase(subjectName.getPlainText())) {
            throw new RuntimeException();
        }
        SchoolSubject schoolSubject = new SchoolSubject();
        schoolSubject.setSubjectName(subjectName.getPlainText());
        return schoolSubjectRepository.save(schoolSubject);
    }

    private boolean doesSubjectAlreadyExistInDatabase(String subjectName) {
        return schoolSubjectRepository.existsBySubjectName(subjectName);
    }
}
