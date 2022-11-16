package pl.schoolmanagementsystem.admin.teacher.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubject;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.common.user.AppUser;
import pl.schoolmanagementsystem.common.user.Role;
import pl.schoolmanagementsystem.common.user.RoleRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static pl.schoolmanagementsystem.admin.mailSender.TokenGenerator.generateToken;

@Component
@RequiredArgsConstructor
public class TeacherCreator {

    private final RoleRepository roleRepository;

    public Teacher createTeacher(TeacherInputDto teacherInputDto, Set<SchoolSubject> taughtSubjects) {
        Teacher teacher = Teacher.builder()
                .name(teacherInputDto.getName())
                .surname(teacherInputDto.getSurname())
                .appUser(new AppUser(teacherInputDto.getEmail(),
                        null,
                        generateToken(),
                        new ArrayList<>()))
                .taughtSubjects(taughtSubjects)
                .teacherInClasses(new HashSet<>())
                .build();
        Role roleTeacher = roleRepository.findById("ROLE_TEACHER").get();
        teacher.getAppUser().getRoles().add(roleTeacher);
        if (teacherInputDto.isAdmin()) {
            Role roleAdmin = roleRepository.findById("ROLE_ADMIN").get();
            teacher.getAppUser().getRoles().add(roleAdmin);
        }
        return teacher;
    }
}
