package pl.schoolmanagementsystem.teacherinclass.utils;

import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.teacher.Teacher;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClass;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClassService;

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
