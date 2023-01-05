package pl.schoolmanagementsystem.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.schoolmanagementsystem.ControllerSamples;
import pl.schoolmanagementsystem.controller.StudentMarkController;
import pl.schoolmanagementsystem.mapper.MarkMapper2;
import pl.schoolmanagementsystem.repository.MarkRepository;
import pl.schoolmanagementsystem.model.dto.MarkAvgDto;
import pl.schoolmanagementsystem.model.dto.MarkDto;
import pl.schoolmanagementsystem.repository.StudentRepository;
import pl.schoolmanagementsystem.exception.handler.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.handler.ValidationErrorHandler;
import pl.schoolmanagementsystem.config.SecurityConfig;
import pl.schoolmanagementsystem.exception.handler.FailureHandler;
import pl.schoolmanagementsystem.exception.handler.SuccessHandler;
import pl.schoolmanagementsystem.service.UserService;
import pl.schoolmanagementsystem.service.StudentMarkService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentMarkController.class)
@ContextConfiguration(classes = MockMvcConfig6.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentMarkControllerTest implements ControllerSamples {

    @MockBean
    private StudentMarkService studentMarkService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_get_for_grouped_marks_by_subject() throws Exception {
        Principal principal = new UserPrincipal("Student");
        Map<String, List<MarkDto>> groupedMarksBySubject = getGroupedMarksBySubject();
        Map<String, List<Byte>> expectedMap = MarkMapper2.mapToListOfBytesInMapStructure(groupedMarksBySubject);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedMap);
        when(studentMarkService.getGroupedMarksBySubject(anyString())).thenReturn(groupedMarksBySubject);

        MvcResult mvcResult = mockMvc.perform(get("/student/marks").principal(principal))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_ok_when_get_for_averages() throws Exception {
        Principal principal = new UserPrincipal("Student");
        List<MarkAvgDto> listOfMarks = List.of(markAvgDto(), markAvgDto());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfMarks);
        when(studentMarkService.getAverageMarks(anyString())).thenReturn(listOfMarks);

        MvcResult mvcResult = mockMvc.perform(get("/student/averages").principal(principal))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }
}

class MockMvcConfig6 {

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
    StudentMarkService studentMarkService() {
        StudentRepository studentRepository = mock(StudentRepository.class);
        MarkRepository markRepository = mock(MarkRepository.class);
        return new StudentMarkService(studentRepository, markRepository);
    }

    @Bean
    StudentMarkController studentMarkController(StudentMarkService studentMarkService) {
        return new StudentMarkController(studentMarkService);
    }
}