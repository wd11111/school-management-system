package pl.schoolmanagementsystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.model.SchoolSubject;
import pl.schoolmanagementsystem.model.Teacher;
import pl.schoolmanagementsystem.model.dto.TeacherRequestDto;
import pl.schoolmanagementsystem.model.AppUser;
import pl.schoolmanagementsystem.model.Role;
import pl.schoolmanagementsystem.repository.RoleRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static pl.schoolmanagementsystem.utils.TokenGenerator.generateToken;

@Component
@RequiredArgsConstructor
public class TeacherCreator {

    private final RoleRepository roleRepository;

    public Teacher createTeacher(TeacherRequestDto teacherRequestDto, Set<SchoolSubject> taughtSubjects) {
        Teacher teacher = Teacher.builder()
                .name(teacherRequestDto.getName())
                .surname(teacherRequestDto.getSurname())
                .appUser(new AppUser(teacherRequestDto.getEmail(),
                        null,
                        generateToken(),
                        new ArrayList<>()))
                .taughtSubjects(taughtSubjects)
                .teacherInClasses(new HashSet<>())
                .build();
        return addRoles(teacher, teacherRequestDto.isAdmin());
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
