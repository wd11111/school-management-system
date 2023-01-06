package pl.schoolmanagementsystem.schoolSubject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.schoolSubject.exception.SubjectAlreadyExistsException;
import pl.schoolmanagementsystem.schoolSubject.mapper.SchoolSubjectCreator;

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
        return schoolSubjectRepository.findAllSchoolSubjects(pageable);
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
