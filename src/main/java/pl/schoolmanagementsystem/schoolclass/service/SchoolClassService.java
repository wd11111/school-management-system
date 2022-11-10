package pl.schoolmanagementsystem.schoolclass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.schoolclass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.schoolsubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.student.repository.StudentRepository;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    private final SchoolSubjectService schoolSubjectService;

    private final StudentRepository studentRepository;

    private final TeacherService teacherService;

    public SchoolClassDto createSchoolClass(SchoolClassDto schoolClassDto) {
        checkIfClassAlreadyExists(schoolClassDto);
        schoolClassRepository.save(buildSchoolClass(schoolClassDto));
        return schoolClassDto;
    }

    public List<StudentOutputDto2> getAllStudentsInClass(String schoolClassName) {
        checkIfClassExists(schoolClassName);
        return studentRepository.findAllStudentsInClass(schoolClassName);
    }

    public List<SchoolClassDto> getListOfClasses() {
        return schoolClassRepository.findAllSchoolClasses();
    }

    public List<SubjectAndTeacherOutputDto> getAllSubjectsForSchoolClass(String schoolClassName) {
        checkIfClassExists(schoolClassName);
        return schoolClassRepository.findAllSubjectsForSchoolClass(schoolClassName);
    }

    public void checkIfThisClassAlreadyHasTeacherOfThisSubject(SchoolClass schoolClass, SchoolSubject schoolSubject) {
        schoolClass.getTeachersInClass().stream()
                .filter(teacher -> teacher.getTaughtSubject().equals(schoolSubject))
                .findFirst()
                .ifPresent(teacher -> {
                    throw new ClassAlreadyHasTeacherException(teacher, schoolSubject, schoolClass);
                });
    }

    public SchoolClass find(String schoolClassName) {
        return schoolClassRepository.findBySchoolClassName(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
    }

    private SchoolClass buildSchoolClass(SchoolClassDto schoolClassDto) {
        return SchoolClass.builder()
                .name(schoolClassDto.getSchoolClassName())
                .build();
    }

    private void checkIfClassExists(String schoolClassName) {
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