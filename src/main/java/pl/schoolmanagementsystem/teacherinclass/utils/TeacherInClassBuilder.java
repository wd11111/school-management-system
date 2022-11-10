package pl.schoolmanagementsystem.teacherinclass.utils;

import pl.schoolmanagementsystem.schoolclass.model.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.model.SchoolSubject;
import pl.schoolmanagementsystem.teacher.model.Teacher;
import pl.schoolmanagementsystem.teacherinclass.model.TeacherInClass;
import pl.schoolmanagementsystem.teacherinclass.service.TeacherInClassService;

public class TeacherInClassBuilder {

    public static TeacherInClass build(Teacher teacher, SchoolSubject schoolSubject, SchoolClass schoolClass,
                                              TeacherInClassService teacherInClassService) {
        TeacherInClass teacherInClass = teacherInClassService.getTeacherInClassIfTheTeacherAlreadyHasEquivalent(teacher, schoolSubject)
                .orElse(new TeacherInClass());
        teacherInClass.getTaughtClasses().add(schoolClass);
        if (hasTeacherInClassBeenJustCreated(teacherInClass)) {
            teacherInClass.setTeacher(teacher);
            teacherInClass.setTaughtSubject(schoolSubject);
        }
        return teacherInClass;
    }

    private static boolean hasTeacherInClassBeenJustCreated(TeacherInClass teacherInClass) {
        return teacherInClass.getTeacher() == null;
    }
}
