package pl.schoolmanagementsystem.student.controller;

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
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.criteria.FilterService;
import pl.schoolmanagementsystem.common.email.service.EmailService;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.common.role.RoleAdder;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;
import pl.schoolmanagementsystem.student.dto.CreateStudentDto;
import pl.schoolmanagementsystem.student.dto.StudentWithClassDto;
import pl.schoolmanagementsystem.student.service.AdminStudentService;
import pl.schoolmanagementsystem.student.utils.StudentMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminStudentController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MockMvcConfig3.class)
class AdminStudentControllerTest implements Samples {

    @MockBean
    private AdminStudentService adminStudentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_created_when_creating_student() throws Exception {
        CreateStudentDto createStudentDto = studentRequestDto();
        String body = objectMapper.writeValueAsString(createStudentDto);
        StudentWithClassDto studentResponse = new StudentWithClassDto(ID_1, NAME, SURNAME, CLASS_1A);
        when(adminStudentService.createStudent(any())).thenReturn(studentResponse);

        MvcResult mvcResult = mockMvc.perform(post("/admin/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isCreated())
                .andReturn();
        StudentWithClassDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), StudentWithClassDto.class);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("studentId")
                .comparingOnlyFields("name", "surname", "schoolClassName").isEqualTo(studentResponse);
    }

    @Test
    void should_return_status_bad_request_when_creating_student() throws Exception {
        CreateStudentDto createStudentDto = new CreateStudentDto();
        String body = objectMapper.writeValueAsString(createStudentDto);

        mockMvc.perform(post("/admin/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isBadRequest());

        then(adminStudentService).shouldHaveNoInteractions();
    }

    @Test
    void should_return_no_content_when_deleting_student() throws Exception {
        doNothing().when(adminStudentService).deleteStudent(anyLong());

        mockMvc.perform(delete("/admin/students/1"))
                .andExpect(status()
                        .isNoContent());
        verify(adminStudentService, times(1)).deleteStudent(anyLong());
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
        EmailService emailService = mock(EmailService.class);
        AppUserRepository userRepository = mock(AppUserRepository.class);
        RoleAdder roleAdder = mock(RoleAdder.class);
        StudentMapper studentMapper = mock(StudentMapper.class);
        FilterService filterService = mock(FilterService.class);
        return new AdminStudentService(studentRepository, schoolClassRepository,
                emailService, userRepository, roleAdder, studentMapper, filterService);
    }

    @Bean
    AdminStudentController adminStudentController(AdminStudentService adminStudentService) {
        return new AdminStudentController(adminStudentService);
    }
}