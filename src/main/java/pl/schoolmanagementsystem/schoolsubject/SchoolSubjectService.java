package pl.schoolmanagementsystem.schoolsubject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.schoolsubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.schoolsubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.schoolsubject.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.schoolsubject.utils.SchoolSubjectBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolSubjectDto createSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        checkIfSubjectAlreadyExists(schoolSubjectDto);
        schoolSubjectRepository.save(SchoolSubjectBuilder.build(schoolSubjectDto));
        return schoolSubjectDto;
    }

    public List<SchoolSubjectDto> getAllSchoolSubjects() {
        return schoolSubjectRepository.findAllSchoolSubjects();
    }

    public List<SubjectAndTeacherOutputDto> getAllSubjectsForSchoolClass(String schoolClassName) {
        return schoolSubjectRepository.findAllSubjectsForSchoolClass(schoolClassName);
    }

    @Transactional
    public void deleteSchoolSubjectByName(String subjectName) {
        checkIfSubjectExists(subjectName);
        schoolSubjectRepository.deleteTaughtSubjects(subjectName);
        schoolSubjectRepository.deleteById(subjectName);
    }

    public SchoolSubject findByNameOrThrow(String name) {
        return schoolSubjectRepository.findBySubjectName(name)
                .orElseThrow(() -> new NoSuchSchoolSubjectException(name));
    }

    private void checkIfSubjectAlreadyExists(SchoolSubjectDto schoolSubjectDto) {
        if (doesSchoolSubjectExistsByName(schoolSubjectDto.getSubjectName())) {
            throw new SubjectAlreadyExistsException(schoolSubjectDto);
        }
    }

    private void checkIfSubjectExists(String subjectName) {
        if (!doesSchoolSubjectExistsByName(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
    }

    private boolean doesSchoolSubjectExistsByName(String schoolSubjectName) {
        return schoolSubjectRepository.existsByName(schoolSubjectName);
    }
}
