package pl.schoolmanagementsystem.teacherinclass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.teacher.Teacher;
import pl.schoolmanagementsystem.teacher.exception.TeacherDoesNotTeachClassException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherInClassService {

    private final TeacherInClassRepository teacherInClassRepository;


    public void makeSureTeacherTeachesThisSubjectInClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        if (teacher.isAdmin()) {
            return;
        }
        TeacherInClass teacherInClass = findByTeacherAndSubjectOrThrow(teacher, schoolSubject, schoolClass);
        if (doesTeacherTeachThisClass(teacherInClass, schoolClass)) {
            throw new TeacherDoesNotTeachClassException(schoolSubject, schoolClass);
        }
    }

    public TeacherInClass addTeacherToClass(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        TeacherInClass teacherInClass = findByTeacherAndSubject(teacher, schoolSubject)
                .orElse(new TeacherInClass());
        teacherInClass.getTaughtClasses().add(schoolClass);
        if (hasTeacherInClassBeenJustCreated(teacherInClass)) {
            teacherInClass.setTeacher(teacher);
            teacherInClass.setTaughtSubject(schoolSubject);
        }
        return teacherInClassRepository.save(teacherInClass);
    }

    public Optional<TeacherInClass> findByTeacherAndSubject(Teacher teacher, SchoolSubject schoolSubject) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject);
    }

    public TeacherInClass findByTeacherAndSubjectOrThrow(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass) {
        return teacherInClassRepository.findByTeacherAndTaughtSubject(teacher, schoolSubject)
                .orElseThrow(() -> new TeacherDoesNotTeachClassException(schoolSubject, schoolClass));
    }

    private boolean doesTeacherTeachThisClass(TeacherInClass teacherInClass, SchoolClass schoolClass) {
        return teacherInClass.getTaughtClasses().contains(schoolClass);
    }

    private boolean hasTeacherInClassBeenJustCreated(TeacherInClass teacherInClass) {
        return teacherInClass.getTeacher() == null;
    }

}