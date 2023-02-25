package pl.schoolmanagementsystem.schoolClass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import pl.schoolmanagementsystem.schoolClass.dto.AddOrRemoveTeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.StudentDto;
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

    public SchoolClass createSchoolClass(SchoolClassDto dto) {
        validateClassNameAvailability(dto.getSchoolClassName());
        return schoolClassRepository.save(schoolClassMapper.mapDtoToEntity(dto));
    }

    public List<TaughtSubjectDto> getTaughtSubjectsInClass(String schoolClassName) {
        validateClassExists(schoolClassName);
        return schoolSubjectRepository.findTaughtSubjectsInClass(schoolClassName);
    }

    public TeacherInClassDto assignTeacherToSchoolClass(AddOrRemoveTeacherInClassDto dto, String schoolClassName) {
        Teacher teacher = teacherRepository.findByIdAndFetchSubjects(dto.getTeacherId())
                .orElseThrow(() -> new NoSuchTeacherException(dto.getTeacherId()));
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
        SchoolSubject schoolSubject = schoolSubjectRepository.findByNameIgnoreCase(dto.getTaughtSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(dto.getTaughtSubject()));

        validateTeacherTeachesSubject(teacher, schoolSubject);
        validateClassDoesntAlreadyHaveTeacher(schoolClass, schoolSubject);

        TeacherInClass teacherInClass = teacherInClassService.assignTeacherToClass(teacher, schoolSubject.getName(), schoolClass);
        return teacherInClassMapper.mapEntityToDto(teacherInClass);
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

    private void validateClassDoesntAlreadyHaveTeacher(SchoolClass schoolClass, SchoolSubject schoolSubject) {
        schoolClass.getTeachersInClass().stream()
                .filter(teacher -> teacher.getTaughtSubject().equals(schoolSubject.getName()))
                .findAny()
                .ifPresent(teacher -> {
                    throw new ClassAlreadyHasTeacherException(teacher, schoolSubject, schoolClass);
                });
    }

    private void validateTeacherTeachesSubject(Teacher teacher, SchoolSubject schoolSubject) {
        if (!teacher.getTaughtSubjects().contains(schoolSubject)) {
            throw new TeacherDoesNotTeachSubjectException(teacher, schoolSubject.getName());
        }
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
