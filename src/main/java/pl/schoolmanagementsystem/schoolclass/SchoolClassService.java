package pl.schoolmanagementsystem.schoolclass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.schoolclass.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.schoolclass.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.schoolclass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    public SchoolClassDto createSchoolClass(SchoolClassDto schoolClassDto) {
        checkIfClassAlreadyExists(schoolClassDto);
        schoolClassRepository.save(SchoolClassCreator.build(schoolClassDto));
        return schoolClassDto;
    }

    public List<SchoolClassDto> getListOfClasses() {
        return schoolClassRepository.findAllSchoolClasses();
    }

    public void makeSureThisClassDoesntHaveTeacherForThisSubject(SchoolClass schoolClass, SchoolSubject schoolSubject) {
        schoolClass.getTeachersInClass().stream()
                .filter(teacher -> teacher.getTaughtSubject().equals(schoolSubject))
                .findFirst()
                .ifPresent(teacher -> {
                    throw new ClassAlreadyHasTeacherException(teacher, schoolSubject, schoolClass);
                });
    }

    public SchoolClass findByNameOrThrow(String schoolClassName) {
        return schoolClassRepository.findBySchoolClassName(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
    }

    public void checkIfClassExists(String schoolClassName) {
        if (!doesSchoolClassExistsByName(schoolClassName)) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
    }

    private void checkIfClassAlreadyExists(SchoolClassDto schoolClassDto) {
        if (doesSchoolClassExistsByName(schoolClassDto.getSchoolClassName())) {
            throw new ClassAlreadyExistsException(schoolClassDto);
        }
    }

    private boolean doesSchoolClassExistsByName(String schoolClassName) {
        return schoolClassRepository.existsByName(schoolClassName);
    }
}