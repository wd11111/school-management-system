package pl.schoolmanagementsystem.admin.schoolClass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.admin.schoolClass.mapper.SchoolClassMapper;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolClass.dto.TeacherInClassInputDto;
import pl.schoolmanagementsystem.common.schoolClass.exception.ClassAlreadyExistsException;
import pl.schoolmanagementsystem.common.schoolClass.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.common.schoolClass.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherResponseDto;
import pl.schoolmanagementsystem.common.schoolSubject.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherInClass;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.common.teacher.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.teacher.exception.TeacherDoesNotTeachSubjectException;

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
        return schoolClassRepository.save(SchoolClassMapper.build(schoolClassDto));
    }

    public List<SubjectAndTeacherResponseDto> getAllSubjectsInSchoolClass(String schoolClassName) {
        if (!doesSchoolClassExist((schoolClassName))) {
            throw new NoSuchSchoolClassException(schoolClassName);
        }
        return schoolSubjectRepository.findAllSubjectsInSchoolClass(schoolClassName);
    }

    public TeacherInClass addTeacherToSchoolClass(TeacherInClassInputDto teacherInClassInputDto, String schoolClassName) {
        Teacher teacher = teacherRepository.findById(teacherInClassInputDto.getTeacherId())
                .orElseThrow(() -> new NoSuchTeacherException(teacherInClassInputDto.getTeacherId()));
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
        SchoolSubject schoolSubject = schoolSubjectRepository.findByNameIgnoreCase(teacherInClassInputDto.getTaughtSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(teacherInClassInputDto.getTaughtSubject()));
        validateIfTeacherTeachesSubject(teacher, schoolSubject);
        validateIfClassDoesntAlreadyHaveTeacher(schoolClass, schoolSubject);
        return teacherInClassService.addTeacherToClass(teacher, schoolSubject.getName(), schoolClass);
    }

    public List<StudentOutputDto2> getAllStudentsInClass(String className) {
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

    private void validateIfClassDoesntAlreadyHaveTeacher(SchoolClass schoolClass, SchoolSubject schoolSubject) {
        schoolClass.getTeachersInClass().stream()
                .filter(teacher -> teacher.getTaughtSubject().equals(schoolSubject.getName()))
                .findAny()
                .ifPresent(teacher -> {
                    throw new ClassAlreadyHasTeacherException(teacher, schoolSubject, schoolClass);
                });
    }

    private void validateIfTeacherTeachesSubject(Teacher teacher, SchoolSubject schoolSubject) {
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
