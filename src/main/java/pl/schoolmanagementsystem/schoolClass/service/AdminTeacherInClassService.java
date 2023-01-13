package pl.schoolmanagementsystem.schoolClass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.model.Teacher;
import pl.schoolmanagementsystem.common.model.TeacherInClass;
import pl.schoolmanagementsystem.common.repository.TeacherInClassRepository;

@Service
@RequiredArgsConstructor
public class AdminTeacherInClassService {

    private final TeacherInClassRepository teacherInClassRepository;

    public TeacherInClass addTeacherToClass(Teacher teacher, String schoolSubject, SchoolClass schoolClass) {
        TeacherInClass teacherInClass = teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject)
                .orElseGet(TeacherInClass::new);

        teacherInClass.getTaughtClasses().add(schoolClass);
        if (hasTeacherInClassBeenJustCreated(teacherInClass)) {
            teacherInClass.setTeacher(teacher);
            teacherInClass.setTaughtSubject(schoolSubject);
        }
        return teacherInClassRepository.save(teacherInClass);
    }

    private boolean hasTeacherInClassBeenJustCreated(TeacherInClass teacherInClass) {
        return teacherInClass.getTaughtClasses().size() == 1;
    }
}
