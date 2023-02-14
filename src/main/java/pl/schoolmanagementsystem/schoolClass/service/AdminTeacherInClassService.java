package pl.schoolmanagementsystem.schoolClass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schoolmanagementsystem.common.exception.*;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.schoolClass.dto.AddOrRemoveTeacherInClassDto;

@Service
@RequiredArgsConstructor
public class AdminTeacherInClassService {

    private final TeacherInClassRepository teacherInClassRepository;
    private final SchoolClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    private final SchoolSubjectRepository schoolSubjectRepository;

    public TeacherInClass addTeacherToClass(Teacher teacher, String schoolSubject, SchoolClass schoolClass) {
        TeacherInClass teacherInClass = teacherInClassRepository.findByTeacherIdAndTaughtSubject(teacher.getId(), schoolSubject)
                .orElseGet(TeacherInClass::new);

        teacherInClass.getTaughtClasses().add(schoolClass);
        if (hasTeacherInClassBeenJustCreated(teacherInClass)) {
            teacherInClass.setTeacher(teacher);
            teacherInClass.setTaughtSubject(schoolSubject);
        }
        return teacherInClassRepository.save(teacherInClass);
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

    private void validateTeacherTeachesClass(TeacherInClass teacher, SchoolClass schoolClass, String subject) {
        if (!doesTeacherTeachClass(teacher, schoolClass.getName())) {
            throw new TeacherDoesNotTeachClassException(teacher.getId(), subject, schoolClass.getName());
        }
    }

    private boolean doesTeacherTeachClass(TeacherInClass teacher, String schoolClassName) {
        return teacher.getTaughtClasses()
                .stream()
                .anyMatch(schoolClass -> schoolClass.getName().equals(schoolClassName));
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
}
