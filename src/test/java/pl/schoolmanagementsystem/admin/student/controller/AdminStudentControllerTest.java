package pl.schoolmanagementsystem.admin.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.schoolmanagementsystem.ControllerSamples;
import pl.schoolmanagementsystem.admin.mailSender.MailSenderService;
import pl.schoolmanagementsystem.admin.student.mapper.StudentCreator;
import pl.schoolmanagementsystem.admin.student.service.AdminStudentService;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.student.Student;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.student.dto.StudentInputDto;
import pl.schoolmanagementsystem.common.user.AppUserRepository;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminStudentController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MockMvcConfig3.class)
class AdminStudentControllerTest implements ControllerSamples {

    @MockBean
    private AdminStudentService adminStudentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_created_when_creating_student() throws Exception {
        StudentInputDto studentInputDto = studentInputDto();
        String body = objectMapper.writeValueAsString(studentInputDto);
        Student student = student();
        when(adminStudentService.createStudent(any())).thenReturn(student);

        MvcResult mvcResult = mockMvc.perform(post("/admin/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isCreated())
                .andReturn();
        Student actualResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Student.class);

        assertThat(actualResponseBody.getName()).isEqualTo(studentInputDto.getName());
    }

    @Test
    void should_return_status_bad_request_when_creating_student() throws Exception {
        StudentInputDto studentInputDto = new StudentInputDto();
        String body = objectMapper.writeValueAsString(studentInputDto);

        mockMvc.perform(post("/admin/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isBadRequest());

        verify(adminStudentService, never()).createStudent(any());
    }

    @Test
    void should_return_no_content_when_deleting_student() throws Exception {
        doNothing().when(adminStudentService).deleteStudent(anyInt());

        mockMvc.perform(delete("/admin/students/1"))
                .andExpect(status()
                        .isNoContent());
    }
}

class MockMvcConfig3 {

    @Bean
    ValidationErrorHandler validationErrorHandler() {
        return new ValidationErrorHandler();
    }

    @Bean
    RestExceptionHandler restExceptionHandler() {
        return new RestExceptionHandler();
    }

    @Bean
    AdminStudentService adminStudentService() {
        StudentRepository studentRepository = mock(StudentRepository.class);
        SchoolClassRepository schoolClassRepository = mock(SchoolClassRepository.class);
        MailSenderService mailSenderService = mock(MailSenderService.class);
        AppUserRepository userRepository = mock(AppUserRepository.class);
        StudentCreator studentCreator = mock(StudentCreator.class);
        return new AdminStudentService(studentRepository, schoolClassRepository,
                mailSenderService, userRepository, studentCreator);
    }

    @Bean
    AdminStudentController adminStudentController(AdminStudentService adminStudentService) {
        return new AdminStudentController(adminStudentService);
    }
}