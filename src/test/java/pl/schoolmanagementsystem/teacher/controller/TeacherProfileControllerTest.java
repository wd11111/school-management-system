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
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.repository.*;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;
import pl.schoolmanagementsystem.security.config.SecurityConfig;
import pl.schoolmanagementsystem.security.handler.FailureHandler;
import pl.schoolmanagementsystem.security.handler.SuccessHandler;
import pl.schoolmanagementsystem.security.service.UserService;
import pl.schoolmanagementsystem.teacher.dto.GiveMarkDto;
import pl.schoolmanagementsystem.teacher.dto.StudentWithMarksDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.service.TeacherProfileService;
import pl.schoolmanagementsystem.teacher.utils.MarkMapper;
import pl.schoolmanagementsystem.teacher.utils.StudentMapper;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.schoolmanagementsystem.common.model.MarkEnum.E_PLUS;

@WebMvcTest(controllers = TeacherProfileController.class)
@ContextConfiguration(classes = MockMvcConfigProfile.class)
@AutoConfigureMockMvc(addFilters = false)
class TeacherProfileControllerTest implements Samples {

    @MockBean
    private TeacherProfileService teacherProfileService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_get_for_taught_classes() throws Exception {
        Principal principal = new UserPrincipal("Teacher");
        Page<SubjectAndClassDto> listOfTaughtClasses = new PageImpl<>(listOfTaughtClasses());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfTaughtClasses);
        when(teacherProfileService.getTaughtClassesByTeacher(anyString(), any())).thenReturn(listOfTaughtClasses);

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
        List<StudentWithMarksDto> listOfStudentsDto = List.of(studentResponseDto3(), studentResponseDto3());
        List<StudentWithMarksDto> listOfStudents = List.of(studentWithMarksDto(), studentWithMarksDto());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfStudentsDto);
        when(teacherProfileService.getClassStudentsWithMarksOfSubject(anyString(), anyString(), anyString())).thenReturn(listOfStudents);

        MvcResult mvcResult = mockMvc.perform(get("/teacher/classes/1a/students?subject=biology").principal(principal))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_no_content_when_giving_marks() throws Exception {
        Principal principal = new UserPrincipal("Teacher");
        GiveMarkDto giveMarkDto = new GiveMarkDto(E_PLUS, SUBJECT_BIOLOGY);
        String body = objectMapper.writeValueAsString(giveMarkDto);
        doNothing().when(teacherProfileService).giveMark(anyString(), any(), anyLong());

        mockMvc.perform(post("/teacher/students/1").principal(principal)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNoContent());

        verify(teacherProfileService, times(1)).giveMark(anyString(), any(), anyLong());
    }

    @Test
    void should_return_status_bad_request_when_giving_marks() throws Exception {
        Principal principal = new UserPrincipal("Teacher");
        GiveMarkDto giveMarkDto = new GiveMarkDto(null, SUBJECT_BIOLOGY);
        String body = objectMapper.writeValueAsString(giveMarkDto);

        mockMvc.perform(post("/teacher/students/1").principal(principal)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());

        then(teacherProfileService).shouldHaveNoInteractions();
    }

}

class MockMvcConfigProfile {

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
    TeacherProfileService teacherClassService() {
        TeacherRepository teacherRepository = mock(TeacherRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        TeacherInClassRepository teacherInClassRepository = mock(TeacherInClassRepository.class);
        SchoolSubjectRepository subjectRepository = mock(SchoolSubjectRepository.class);
        SchoolClassRepository classRepository = mock(SchoolClassRepository.class);
        MarkMapper markMapper = mock(MarkMapper.class);
        pl.schoolmanagementsystem.teacher.utils.StudentMapper studentMapper = mock(StudentMapper.class);
        return new TeacherProfileService(teacherRepository, studentRepository, teacherInClassRepository,
                subjectRepository, classRepository, studentMapper, markMapper);
    }

    @Bean
    TeacherProfileController teacherClassController(TeacherProfileService teacherProfileService) {
        return new TeacherProfileController(teacherProfileService);
    }
}