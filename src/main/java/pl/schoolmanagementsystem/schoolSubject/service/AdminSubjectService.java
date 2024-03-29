package pl.schoolmanagementsystem.schoolSubject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.schoolSubject.utils.SchoolSubjectMapper;

@Service
@RequiredArgsConstructor
public class AdminSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final SchoolSubjectMapper schoolSubjectMapper;

    public SchoolSubject createSchoolSubject(SchoolSubjectDto dto) {
        validateSubjectNameAvailability(dto.getSubjectName());
        return schoolSubjectRepository.save(schoolSubjectMapper.mapDtoToEntity(dto));
    }

    public Page<SchoolSubjectDto> getAllSubjects(Pageable pageable) {
        return schoolSubjectRepository.findAllSchoolSubjects(pageable);
    }

    @Transactional
    public void deleteSchoolSubject(String subjectName) {
        validateSubjectExists(subjectName);

        schoolSubjectRepository.deleteTaughtSubjects(subjectName);
        schoolSubjectRepository.deleteById(subjectName);
    }

    private void validateSubjectExists(String subjectName) {
        if (!doesSubjectExist(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
    }

    private void validateSubjectNameAvailability(String subjectName) {
        if (doesSubjectExist(subjectName)) {
            throw new SubjectAlreadyExistsException(subjectName);
        }
    }

    private boolean doesSubjectExist(String schoolSubjectName) {
        return schoolSubjectRepository.existsById(schoolSubjectName);
    }
}
