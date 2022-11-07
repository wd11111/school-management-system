package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolSubjectDto createSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        checkIfSubjectAlreadyExists(schoolSubjectDto);
        schoolSubjectRepository.save(buildSchoolSubject(schoolSubjectDto));
        return schoolSubjectDto;
    }

    public List<SchoolSubjectDto> getAllSchoolSubjects() {
        return schoolSubjectRepository.findAllSchoolSubjects();
    }

    @Transactional
    public void deleteSchoolSubjectByName(SchoolSubjectDto schoolSubjectDto) {
        checkIfSubjectExists(schoolSubjectDto);
        schoolSubjectRepository.deleteTaughtSubjects(schoolSubjectDto.getSubject());
        schoolSubjectRepository.deleteById(schoolSubjectDto.getSubject());
    }

    private void checkIfSubjectAlreadyExists(SchoolSubjectDto schoolSubjectDto) {
        if (doesSchoolSubjectExistsByName(schoolSubjectDto.getSubject())) {
            throw new SubjectAlreadyExistsException(schoolSubjectDto);
        }
    }

    private void checkIfSubjectExists(SchoolSubjectDto schoolSubjectDto) {
        if (!doesSchoolSubjectExistsByName(schoolSubjectDto.getSubject())) {
            throw new NoSuchSchoolSubjectException(schoolSubjectDto.getSubject());
        }
    }

    private boolean doesSchoolSubjectExistsByName(String schoolSubjectName) {
        return schoolSubjectRepository.existsBySubjectName(schoolSubjectName);
    }

    private SchoolSubject buildSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        return SchoolSubject.builder()
                .subjectName(schoolSubjectDto.getSubject())
                .build();
    }
}
