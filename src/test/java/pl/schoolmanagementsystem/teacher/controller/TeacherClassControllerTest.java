package pl.schoolmanagementsystem.teacher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.schoolmanagementsystem.ControllerSamples;
import pl.schoolmanagementsystem.common.mark.dto.MarkDto;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.teacher.dto.StudentResponseDto3;
import pl.schoolmanagementsystem.common.teacher.TeacherInClassRepository;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;
import pl.schoolmanagementsystem.security.config.SecurityConfig;
import pl.schoolmanagementsystem.security.handler.FailureHandler;
import pl.schoolmanagementsystem.security.handler.SuccessHandler;
import pl.schoolmanagementsystem.security.service.UserService;
import pl.schoolmanagementsystem.teacher.service.TeacherClassService;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TeacherClassController.class)
@ContextConfiguration(classes = MockMvcConfig5.class)
@AutoConfigureMockMvc(addFilters = false)
class TeacherClassControllerTest implements ControllerSamples {

    @MockBean
    private TeacherClassService teacherClassService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_get_for_taught_classes() throws Exception {
        Principal principal = new UserPrincipal("Teacher");
        Page<SubjectAndClassDto> listOfTaughtClasses = new PageImpl<>(listOfTaughtClasses());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfTaughtClasses);
        when(teacherClassService.getTaughtClassesByTeacher(anyString(), any())).thenReturn(listOfTaughtClasses);

        MvcResult mvcResult = mockMvc.perform(get("/teacher/classes").principal(principal))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_ok_when_get_for_all_students_in_class() throws Exception {
        Principal principal = new UserPrincipal("Teacher");
        List<StudentResponseDto3> listOfStudentsDto = List.of(studentResponseDto3(), studentResponseDto3());
        List<Student> listOfStudents = List.of(student(), student());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfStudentsDto);
        when(teacherClassService.getClassStudentsWithMarksOfSubject(anyString(), anyString(), anyString())).thenReturn(listOfStudents);

        MvcResult mvcResult = mockMvc.perform(get("/teacher/classes/1a?subject=biology").principal(principal))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_no_content_when_adding_marks() throws Exception {
        Principal principal = new UserPrincipal("Teacher");
        MarkDto markDto = new MarkDto(MARK, SUBJECT_BIOLOGY);
        String body = objectMapper.writeValueAsString(markDto);
        doNothing().when(teacherClassService).addMark(anyString(), any(), anyLong());

        mockMvc.perform(patch("/teacher/students/1").principal(principal)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNoContent());

        verify(teacherClassService, times(1)).addMark(anyString(), any(), anyLong());
    }

    @Test
    void should_return_status_bad_request_when_adding_marks() throws Exception {
        Principal principal = new UserPrincipal("Teacher");
        MarkDto markDto = new MarkDto(OUT_OF_RANGE_MARK, SUBJECT_BIOLOGY);
        String body = objectMapper.writeValueAsString(markDto);

        mockMvc.perform(patch("/teacher/students/1").principal(principal)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());

        then(teacherClassService).shouldHaveNoInteractions();
    }

}

class MockMvcConfig5 {

    @Bean
    SecurityConfig securityConfig() {
        UserService userService = mock(UserService.class);
        SuccessHandler successHandler = mock(SuccessHandler.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        FailureHandler failureHandler = mock(FailureHandler.class);
        AuthenticationEntryPoint authenticationEntryPoint = mock(AuthenticationEntryPoint.class);
        return new SecurityConfig(userService, successHandler, objectMapper, failureHandler, authenticationEntryPoint);
    }

    @Bean
    ValidationErrorHandler validationErrorHandler() {
        return new ValidationErrorHandler();
    }

    @Bean
    RestExceptionHandler restExceptionHandler() {
        return new RestExceptionHandler();
    }

    @Bean
    TeacherClassService teacherClassService() {
        TeacherRepository teacherRepository = mock(TeacherRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        TeacherInClassRepository teacherInClassRepository = mock(TeacherInClassRepository.class);
        SchoolSubjectRepository subjectRepository = mock(SchoolSubjectRepository.class);
        SchoolClassRepository classRepository = mock(SchoolClassRepository.class);
        return new TeacherClassService(teacherRepository, studentRepository, teacherInClassRepository,
                subjectRepository, classRepository);
    }

    @Bean
    TeacherClassController teacherClassController(TeacherClassService teacherClassService) {
        return new TeacherClassController(teacherClassService);
    }
}