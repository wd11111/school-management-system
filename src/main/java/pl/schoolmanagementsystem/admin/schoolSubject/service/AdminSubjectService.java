package pl.schoolmanagementsystem.admin.schoolSubject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.schoolSubject.exception.SubjectAlreadyExistsException;

import java.util.List;

import static pl.schoolmanagementsystem.admin.schoolSubject.mapper.SchoolSubjectMapper.createSubject;

@Service
@RequiredArgsConstructor
public class AdminSubjectService {

    private final SchoolSubjectRepository schoolSubjectRepository;

    public SchoolSubject createSchoolSubject(SchoolSubjectDto schoolSubjectDto) {
        if (doesSubjectExist(schoolSubjectDto.getSubjectName())) {
            throw new SubjectAlreadyExistsException(schoolSubjectDto);
        }
        return schoolSubjectRepository.save(createSubject(schoolSubjectDto));
    }

    public List<SchoolSubjectDto> getAllSubjects() {
        return schoolSubjectRepository.findAllSchoolSubjects();
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
