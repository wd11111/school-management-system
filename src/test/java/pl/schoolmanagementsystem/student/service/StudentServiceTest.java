package pl.schoolmanagementsystem.student.service;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.schoolclass.service.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.service.SchoolSubjectService;
import pl.schoolmanagementsystem.student.repository.StudentRepository;
import pl.schoolmanagementsystem.student.utils.StudentMapper;
import pl.schoolmanagementsystem.teacher.service.TeacherService;

import static org.mockito.Mockito.mock;

class StudentServiceTest {

    private StudentRepository studentRepository = mock(StudentRepository.class);

    private EmailService emailService = mock(EmailService.class);

    private StudentMapper studentMapper = mock(StudentMapper.class);

    private SchoolSubjectService schoolSubjectService = mock(SchoolSubjectService.class);

    private TeacherService teacherService = mock(TeacherService.class);

    private SchoolClassService schoolClassService = mock(SchoolClassService.class);

    private StudentService studentService = new StudentService(studentRepository, emailService, studentMapper, schoolSubjectService, teacherService, schoolClassService);

    @Test
    void should_save_student_when_email_is_available() {

    }

}