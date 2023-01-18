package pl.schoolmanagementsystem.schoolClass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.dto.StudentDto;
import pl.schoolmanagementsystem.common.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.exception.*;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.schoolClass.dto.AddTeacherToClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.utils.SchoolClassMapper;
import pl.schoolmanagementsystem.schoolClass.utils.TeacherInClassMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminClassService {

    private final SchoolClassRepository schoolClassRepository;

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final AdminTeacherInClassService teacherInClassService;

    private final StudentRepository studentRepository;

    private final SchoolClassMapper schoolClassMapper;

    private final TeacherInClassMapper teacherInClassMapper;

    public Page<SchoolClassDto> getSchoolClasses(Pageable pageable) {
        return schoolClassRepository.findAllClasses(pageable);
    }

    public SchoolClass createSchoolClass(SchoolClassDto schoolClassDto) {
        if (doesSchoolClassExist(schoolClassDto.getSchoolClassName())) {
            throw new ClassAlreadyExistsException(schoolClassDto);
        }
        return schoolClassRepository.save(schoolClassMapper.mapDtoToEntity(schoolClassDto));
    }

    public List<TaughtSubjectDto> getTaughtSubjectsInClass(String schoolClassName) {
        if (!doesSchoolClassExist((schoolClassName))) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
        return schoolSubjectRepository.findTaughtSubjectsInClass(schoolClassName);
    }

    public TeacherInClassDto addTeacherToSchoolClass(AddTeacherToClassDto addTeacherToClassDto, String schoolClassName) {
        Teacher teacher = teacherRepository.findByIdAndFetchSubjects(addTeacherToClassDto.getTeacherId())
                .orElseThrow(() -> new NoSuchTeacherException(addTeacherToClassDto.getTeacherId()));
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
        SchoolSubject schoolSubject = schoolSubjectRepository.findByNameIgnoreCase(addTeacherToClassDto.getTaughtSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(addTeacherToClassDto.getTaughtSubject()));

        validateTeacherTeachesSubject(teacher, schoolSubject);
        validateClassDoesntAlreadyHaveTeacher(schoolClass, schoolSubject);
        TeacherInClass teacherInClass = teacherInClassService.addTeacherToClass(teacher, schoolSubject.getName(), schoolClass);
        return teacherInClassMapper.mapEntityToDto(teacherInClass);
    }

    public List<StudentDto> getAllStudentsInClass(String className) {
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
