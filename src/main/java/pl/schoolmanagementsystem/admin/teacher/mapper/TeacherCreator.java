package pl.schoolmanagementsystem.admin.teacher.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.admin.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.common.user.Role;
import pl.schoolmanagementsystem.common.user.RoleRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static pl.schoolmanagementsystem.admin.common.mail.TokenGenerator.generateToken;

@Component
@RequiredArgsConstructor
public class TeacherCreator {

    private final RoleRepository roleRepository;

    public Teacher createTeacher(CreateTeacherDto createTeacherDto, Set<SchoolSubject> taughtSubjects) {
        Teacher teacher = Teacher.builder()
                .name(createTeacherDto.getName())
                .surname(createTeacherDto.getSurname())
                .appUser(new AppUser(createTeacherDto.getEmail(),
                        null,
                        generateToken(),
                        new ArrayList<>()))
                .taughtSubjects(taughtSubjects)
                .teacherInClasses(new HashSet<>())
                .build();
        return addRoles(teacher, createTeacherDto.isAdmin());
    }

    private Teacher addRoles(Teacher teacher, boolean isAdmin) {
        Role roleTeacher = roleRepository.findById("ROLE_TEACHER").get();
        teacher.getAppUser().getRoles().add(roleTeacher);
        if (isAdmin) {
            Role roleAdmin = roleRepository.findById("ROLE_ADMIN").get();
            teacher.getAppUser().getRoles().add(roleAdmin);
        }
        return teacher;
    }
}
