package pl.schoolmanagementsystem.student.utils;

import pl.schoolmanagementsystem.common.model.AppUser;
import pl.schoolmanagementsystem.common.model.Student;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentSearchDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;

import java.util.List;

public class StudentMapperStub implements StudentMapper {

    @Override
    public StudentSearchDto mapEntityToSearchDto(Student student) {
        StudentSearchDto studentSearchDto = new StudentSearchDto();
        studentSearchDto.setId(student.getId());
        studentSearchDto.setName(student.getName());
        studentSearchDto.setSurname(student.getSurname());
        studentSearchDto.setBirthDate(student.getBirthDate());
        studentSearchDto.setSchoolClass(student.getSchoolClass());
        return studentSearchDto;
    }

    @Override
    public List<StudentSearchDto> mapEntitiesToSearchDtos(List<Student> students) {
        return students.stream()
                .map(this::mapEntityToSearchDto)
                .toList();
    }

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
