package pl.schoolmanagementsystem.schoolClass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.exception.ClassAlreadyHasTeacherException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolClassException;
import pl.schoolmanagementsystem.common.exception.NoSuchSchoolSubjectException;
import pl.schoolmanagementsystem.common.exception.NoSuchTeacherException;
import pl.schoolmanagementsystem.common.exception.TeacherDoesNotTeachClassException;
import pl.schoolmanagementsystem.common.exception.TeacherDoesNotTeachSubjectException;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.schoolClass.dto.AddOrRemoveTeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.utils.TeacherInClassMapper;

@Service
@RequiredArgsConstructor
public class AdminTeacherInClassService {

    private final TeacherInClassRepository teacherInClassRepository;

    private final SchoolClassRepository classRepository;

    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    private final TeacherInClassMapper teacherInClassMapper;


    public TeacherInClassDto assignTeacherToSchoolClass(AddOrRemoveTeacherInClassDto dto, String schoolClassName) {
        Teacher teacher = teacherRepository.findByIdAndFetchSubjects(dto.getTeacherId())
                .orElseThrow(() -> new NoSuchTeacherException(dto.getTeacherId()));
        SchoolClass schoolClass = classRepository.findById(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));
        SchoolSubject schoolSubject = schoolSubjectRepository.findByNameIgnoreCase(dto.getTaughtSubject())
                .orElseThrow(() -> new NoSuchSchoolSubjectException(dto.getTaughtSubject()));

        validateTeacherTeachesSubject(teacher, schoolSubject);
        validateClassDoesntAlreadyHaveTeacher(schoolClass, schoolSubject);

        TeacherInClass teacherInClass = assignTeacherToClass(teacher, schoolSubject.getName(), schoolClass);
        return teacherInClassMapper.mapEntityToDto(teacherInClass);
    }

    @Transactional
    public void removeTeacherFromSchoolClass(AddOrRemoveTeacherInClassDto dto, String schoolClassName) {
        validateSchoolSubjectExists(dto.getTaughtSubject());

        Teacher teacher = teacherRepository.findByIdAndFetchClasses(dto.getTeacherId())
                .orElseThrow(() -> new NoSuchTeacherException(dto.getTeacherId()));
        SchoolClass schoolClass = classRepository.findById(schoolClassName)
                .orElseThrow(() -> new NoSuchSchoolClassException(schoolClassName));

        TeacherInClass teacherInClass = findTeacherOfSubject(teacher, dto.getTaughtSubject());
        validateTeacherTeachesClass(teacherInClass, schoolClass, dto.getTaughtSubject());

        teacherInClass.removeFromClass(schoolClass);
    }

    private TeacherInClass assignTeacherToClass(Teacher teacher, String schoolSubject, SchoolClass schoolClass) {
        TeacherInClass teacherInClass = teacherInClassRepository.findByTeacherIdAndTaughtSubject(teacher.getId(), schoolSubject)
                .orElseGet(TeacherInClass::new);

        teacherInClass.assignToClass(schoolClass);
        if (hasTeacherInClassBeenJustCreated(teacherInClass)) {
            teacherInClass.setTeacher(teacher);
            teacherInClass.setTaughtSubject(schoolSubject);
        }
        return teacherInClassRepository.save(teacherInClass);
    }

    private void validateTeacherTeachesClass(TeacherInClass teacher, SchoolClass schoolClass, String subject) {
        boolean doesTeacherTeachClass = teacher.getTaughtClasses()
                .stream()
                .anyMatch(schClass -> schClass.getName().equals(schoolClass.getName()));

        if (!doesTeacherTeachClass) {
            throw new TeacherDoesNotTeachClassException(teacher.getId(), subject, schoolClass.getName());
        }
    }

    private TeacherInClass findTeacherOfSubject(Teacher teacher, String subject) {
        return teacher.getTeacherInClasses()
                .stream()
                .filter(teacherInClass -> teacherInClass.getTaughtSubject().equals(subject))
                .findFirst()
                .orElseThrow(() -> new TeacherDoesNotTeachSubjectException(teacher, subject));
    }

    private void validateSchoolSubjectExists(String subjectName) {
        if (!schoolSubjectRepository.existsById(subjectName)) {
            throw new NoSuchSchoolSubjectException(subjectName);
        }
    }

    private boolean hasTeacherInClassBeenJustCreated(TeacherInClass teacherInClass) {
        return teacherInClass.getTaughtClasses().size() == 1;
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

}
