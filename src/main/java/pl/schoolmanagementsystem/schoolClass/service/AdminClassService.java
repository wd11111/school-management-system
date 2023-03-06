package pl.schoolmanagementsystem.schoolClass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.StudentDto;
import pl.schoolmanagementsystem.schoolClass.utils.SchoolClassMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminClassService {

    private final SchoolClassRepository schoolClassRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final StudentRepository studentRepository;

    private final SchoolClassMapper schoolClassMapper;

    public Page<SchoolClassDto> getSchoolClasses(Pageable pageable) {
        return schoolClassRepository.findAllClasses(pageable);
    }

    public SchoolClass createSchoolClass(SchoolClassDto dto) {
        validateClassNameAvailability(dto.getSchoolClassName());
        return schoolClassRepository.save(schoolClassMapper.mapDtoToEntity(dto));
    }

    public List<TaughtSubjectDto> getTaughtSubjectsInClass(String schoolClassName) {
        validateClassExists(schoolClassName);
        return schoolSubjectRepository.findTaughtSubjectsInClass(schoolClassName);
    }

    public List<StudentDto> getAllStudentsInClass(String schoolClassName) {
        validateClassExists(schoolClassName);
        return studentRepository.findAllInClass(schoolClassName);
    }

    @Transactional
    public void deleteSchoolClass(String schoolClassName) {
        validateClassExists(schoolClassName);

        schoolClassRepository.deleteTaughtClasses(schoolClassName);
        schoolClassRepository.deleteById(schoolClassName);
    }

    private void validateClassNameAvailability(String schoolClassName) {
        if (doesSchoolClassExist(schoolClassName)) {
            throw new ClassAlreadyExistsException(schoolClassName);
        }
    }

    private void validateClassExists(String schoolClassName) {
        if (!doesSchoolClassExist(schoolClassName)) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
    }

    private boolean doesSchoolClassExist(String schoolClassName) {
        return schoolClassRepository.existsById(schoolClassName);
    }

}
