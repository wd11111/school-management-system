package pl.schoolmanagementsystem.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.schoolmanagementsystem.email.EmailService;
import pl.schoolmanagementsystem.schoolclass.SchoolClass;
import pl.schoolmanagementsystem.schoolclass.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubject;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubjectService;
import pl.schoolmanagementsystem.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.student.dto.StudentOutputDto3;
import pl.schoolmanagementsystem.teacher.Teacher;
import pl.schoolmanagementsystem.teacher.TeacherService;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClassService;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentFacade {

    private final EmailService emailService;

    private final StudentMapper studentMapper;

    private final SchoolSubjectService schoolSubjectService;

    private final SchoolClassService schoolClassService;

    private final TeacherService teacherService;

    private final TeacherInClassService teacherInClassService;

    private final StudentService studentService;

    public StudentOutputDto createStudent(StudentInputDto studentInputDto) {
        emailService.checkIfEmailIsAvailable(studentInputDto.getEmail());
        SchoolClass schoolClass = schoolClassService.findByNameOrThrow(studentInputDto.getName());
        Student mappedStudent = studentMapper.mapInputDtoToStudent(studentInputDto, schoolClass);
        Student student = studentService.save(mappedStudent);
        return studentMapper.mapStudentToOutputDto(student);
    }

    public List<StudentOutputDto3> getAllStudentsInClassWithMarksOfTheSubject(String schoolClassName,
                                                                              String subjectName, int teacherId) {
        SchoolClass schoolClass = schoolClassService.findByNameOrThrow(schoolClassName);
        SchoolSubject schoolSubject = schoolSubjectService.findByNameOrThrow(subjectName);
        Teacher teacher = teacherService.findByIdOrThrow(teacherId);
        teacherInClassService.makeSureTeacherTeachesThisSubjectInClass(teacher, schoolSubject, schoolClass);
        return studentService.getAllStudentsInClassWithMarksOfTheSubject(schoolClassName, subjectName);
    }

    public List<StudentOutputDto2> getAllStudentsInClass(String schoolClassName) {
        schoolClassService.checkIfClassExists(schoolClassName);
        return studentService.getAllStudentsInClass(schoolClassName);
    }

    public void deleteStudent(int studentId) {
        studentService.checkIfStudentExists(studentId);
        studentService.deleteStudent(studentId);
    }

    public int getIdFromPrincipals(Principal principal) {
        return studentService.getIdFromPrincipals(principal);
    }
}
