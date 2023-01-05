package pl.schoolmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.mapper.SchoolClassCreator;
import pl.schoolmanagementsystem.model.SchoolClass;
import pl.schoolmanagementsystem.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.model.dto.SchoolClassDto;
import pl.schoolmanagementsystem.model.dto.TeacherInClassRequestDto;
import pl.schoolmanagementsystem.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.model.dto.SubjectAndTeacherResponseDto;
import pl.schoolmanagementsystem.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.model.dto.StudentResponseDto2;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.TeacherInClass;
import pl.schoolmanagementsystem.repository.TeacherRepository;
import pl.schoolmanagementsystem.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.exception.TeacherDoesNotTeachSubjectException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminClassService {

    private final SchoolClassRepository schoolClassRepository;

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final AdminTeacherInClassService teacherInClassService;

    private final StudentRepository studentRepository;

    public Page<SchoolClassDto> getSchoolClasses(Pageable pageable) {
        return schoolClassRepository.findAllClasses(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    public SchoolClass createSchoolClass(SchoolClassDto schoolClassDto) {
        if (doesSchoolClassExist(schoolClassDto.getSchoolClassName())) {
            throw new ClassAlreadyExistsException(schoolClassDto);
        }
        return schoolClassRepository.save(SchoolClassCreator.createSchoolClass(schoolClassDto));
    }

    public List<SubjectAndTeacherResponseDto> getTaughtSubjectsInClass(String schoolClassName) {
        if (!doesSchoolClassExist((schoolClassName))) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
        return schoolSubjectRepository.findTaughtSubjectsInClass(schoolClassName);
    }

    public TeacherInClass addTeacherToSchoolClass(TeacherInClassRequestDto teacherInClassRequestDto, String schoolClassName) {
        Teacher teacher = teacherRepository.findById(teacherInClassRequestDto.getTeacherId())
                .orElseThrow(() -> new NoSuchTeacherException(teacherInClassRequestDto.getTeacherId()));
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
        SchoolSubject schoolSubject = schoolSubjectRepository.findByNameIgnoreCase(teacherInClassRequestDto.getTaughtSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(teacherInClassRequestDto.getTaughtSubject()));
        validateTeacherTeachesSubject(teacher, schoolSubject);
        validateClassDoesntAlreadyHaveTeacher(schoolClass, schoolSubject);
        return teacherInClassService.addTeacherToClass(teacher, schoolSubject.getName(), schoolClass);
    }

    public List<StudentResponseDto2> getAllStudentsInClass(String className) {
        if (!schoolClassRepository.existsById(className)) {
            throw new NoSuchSchoolClassException(className);
        }
        return studentRepository.findAllInClass(className);
    }

    @Transactional
    public void deleteSchoolClass(String schoolClassName) {
        if (!doesSchoolClassExist(schoolClassName)) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
        schoolClassRepository.deleteTaughtClasses(schoolClassName);
        schoolClassRepository.deleteById(schoolClassName);
    }

    private void validateClassDoesntAlreadyHaveTeacher(SchoolClass schoolClass, SchoolSubject schoolSubject) {
        schoolClass.getTeachersInClass().stream()
                .filter(teacher -> teacher.getTaughtSubject().equals(schoolSubject.getName()))
                .findAny()
                .ifPresent(teacher -> {
                    throw new ClassAlreadyHasTeacherException(teacher, schoolSubject, schoolClass);
                });
    }

    private void validateTeacherTeachesSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (!doesTeacherTeachSubject(teacher, schoolSubject)) {
            throw new TeacherDoesNotTeachSubjectException(teacher, schoolSubject);
        }
    }

    private boolean doesTeacherTeachSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacher.getTaughtSubjects().contains(schoolSubject);
    }

    private boolean doesSchoolClassExist(String schoolClassName) {
        return schoolClassRepository.existsById(schoolClassName);
    }
}
