package pl.schoolmanagementsystem.student.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.schoolmanagementsystem.email.Email;
import pl.schoolmanagementsystem.mark.uitls.MarkMapper;
import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.student.Student;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final PasswordEncoder passwordEncoder;

    public StudentOutputDto mapStudentToOutputDto(Student student) {
        return new StudentOutputDto(student.getId(), student.getName(),
                student.getSurname(), student.getSchoolClass().getName());
    }

    public StudentOutputDto3 mapStudentToOutputDto3(Student student) {
        return new StudentOutputDto3(student.getId(), student.getName(),
                student.getSurname(), MarkMapper.mapListOfMarksToIntegers(student.getMarks()));
    }

    public Student mapInputDtoToStudent(StudentInputDto studentInputDto, SchoolClass schoolClass) {
        return Student.builder()
                .name(studentInputDto.getName())
                .surname(studentInputDto.getSurname())
                .email(new Email(studentInputDto.getEmail()))
                .password(passwordEncoder.encode(studentInputDto.getPassword()))
                .schoolClass(schoolClass)
                .build();
    }
}
