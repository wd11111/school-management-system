package pl.schoolmanagementsystem.student.service;

import org.junit.jupiter.api.Test;
import pl.schoolmanagementsystem.email.EmailService;
import pl.schoolmanagementsystem.schoolclass.SchoolClassService;
import pl.schoolmanagementsystem.schoolsubject.SchoolSubjectService;
import pl.schoolmanagementsystem.student.StudentRepository;
import pl.schoolmanagementsystem.student.StudentService;
import pl.schoolmanagementsystem.student.utils.StudentMapper;
import pl.schoolmanagementsystem.teacher.TeacherService;
import pl.schoolmanagementsystem.teacherinclass.TeacherInClassService;

import static org.mockito.Mockito.mock;

class StudentServiceTest {

    private StudentRepository studentRepository = mock(StudentRepository.class);

    private EmailService emailService = mock(EmailService.class);

    private StudentMapper studentMapper = mock(StudentMapper.class);

    private SchoolSubjectService schoolSubjectService = mock(SchoolSubjectService.class);

    private TeacherService teacherService = mock(TeacherService.class);

    private SchoolClassService schoolClassService = mock(SchoolClassService.class);

    private TeacherInClassService teacherInClassService = mock(TeacherInClassService.class);

    private StudentService studentService = new StudentService(studentRepository, emailService, studentMapper,
            schoolSubjectService, teacherService, schoolClassService,teacherInClassService);

    @Test
    void should_save_student_when_email_is_available() {

    }

}