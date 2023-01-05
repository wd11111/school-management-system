package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.mapper.SchoolSubjectCreator;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.model.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.exception.SubjectAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class AdminSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolSubject createSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        if (doesSubjectExist(schoolSubjectDto.getSubjectName())) {
            throw new SubjectAlreadyExistsException(schoolSubjectDto);
        }
        return schoolSubjectRepository.save(SchoolSubjectCreator.createSchoolSubject(schoolSubjectDto));
    }

    public Page<SchoolSubjectDto> getAllSubjects(Pageable pageable) {
        return schoolSubjectRepository.findAllSchoolSubjects(PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize()));
    }

    @Transactional
    public void deleteSchoolSubject(String subjectName) {
        if (!doesSubjectExist(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
        schoolSubjectRepository.deleteTaughtSubjects(subjectName);
        schoolSubjectRepository.deleteById(subjectName);
    }
    private boolean doesSubjectExist(String schoolSubjectName) {
        return schoolSubjectRepository.existsById(schoolSubjectName);
    }
}
