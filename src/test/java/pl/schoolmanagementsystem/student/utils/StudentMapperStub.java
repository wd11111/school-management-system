package pl.schoolmanagementsystem.student.utils;

import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;

public class StudentMapperStub implements StudentMapper {

    @Override
    public Student mapCreateDtoToEntity(CreateStudentDto createStudentDto, AppUser appUser) {
        return Student.builder()
                .name(createStudentDto.getName())
                .surname(createStudentDto.getSurname())
                .appUser(appUser)
                .schoolClass(createStudentDto.getSchoolClass())
                .build();
    }

    @Override
    public StudentWithClassDto mapEntityToDtoWithSchoolClass(Student student) {
        return new StudentWithClassDto(student.getId(), student.getName(), student.getSurname(), student.getSchoolClass());
    }
}
