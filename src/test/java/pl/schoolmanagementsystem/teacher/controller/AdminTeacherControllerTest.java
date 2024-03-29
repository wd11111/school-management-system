package pl.schoolmanagementsystem.teacher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.schoolmanagementsystem.Samples;
import pl.schoolmanagementsystem.common.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.email.service.EmailService;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.common.role.RoleAdder;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;
import pl.schoolmanagementsystem.teacher.dto.CreateTeacherDto;
import pl.schoolmanagementsystem.teacher.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.teacher.dto.TeacherDto;
import pl.schoolmanagementsystem.teacher.service.AdminTeacherService;
import pl.schoolmanagementsystem.teacher.utils.TeacherMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminTeacherController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MockMvcConfigAdmin.class)
class AdminTeacherControllerTest implements Samples {

    @MockBean
    private AdminTeacherService adminTeacherService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_get_for_all_teachers() throws Exception {
        Page<TeacherDto> listOfTeachers = new PageImpl<>(List.of(teacherResponseDto(), teacherResponseDto()));
        String expectedResponseBody = objectMapper.writeValueAsString(listOfTeachers);
        when(adminTeacherService.getAllTeachers(any())).thenReturn(listOfTeachers);

        MvcResult mvcResult = mockMvc.perform(get("/admin/teachers"))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_ok_when_get_for_taught_classes() throws Exception {
        Page<SubjectAndClassDto> listOfTaughtClasses = new PageImpl<>(listOfTaughtClasses());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfTaughtClasses);
        when(adminTeacherService.getTaughtClassesByTeacher(anyLong(), any())).thenReturn(listOfTaughtClasses);

        MvcResult mvcResult = mockMvc.perform(get("/admin/teachers/3/taught-classes"))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_created_when_creating_teacher() throws Exception {
        CreateTeacherDto createTeacherDto = createCreateTeacherDto();
        TeacherDto teacherDto = createTeacherDto();
        String body = objectMapper.writeValueAsString(createTeacherDto);
        when(adminTeacherService.createTeacher(any())).thenReturn(teacherDto);

        MvcResult mvcResult = mockMvc.perform(post("/admin/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isCreated())
                .andReturn();
        TeacherDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TeacherDto.class);

        assertThat(result).usingRecursiveComparison().comparingOnlyFields("name", "surname", "taughtSubjects")
                .isEqualTo(teacherDto);
    }

    @Test
    void should_return_status_bad_request_when_creating_teacher() throws Exception {
        CreateTeacherDto createTeacherDto = new CreateTeacherDto();
        String body = objectMapper.writeValueAsString(createTeacherDto);

        mockMvc.perform(post("/admin/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isBadRequest());

        then(adminTeacherService).shouldHaveNoInteractions();
    }

    @Test
    void should_return_status_ok_when_assigning_subject_to_teacher() throws Exception {
        SchoolSubjectDto schoolSubjectDto = schoolSubjectDto();
        TeacherDto teacherDto = teacherResponseDto();
        String expectedResponse = objectMapper.writeValueAsString(teacherDto);
        String body = objectMapper.writeValueAsString(schoolSubjectDto);
        when(adminTeacherService.assignSubjectToTeacher(anyLong(), any())).thenReturn(teacherDto);

        MvcResult mvcResult = mockMvc.perform(patch("/admin/teachers/1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualTo(expectedResponse);
    }

    @Test
    void should_return_status_bad_request_when_assigning_subject_to_teacher() throws Exception {
        SchoolSubjectDto schoolSubjectDto = new SchoolSubjectDto();
        String body = objectMapper.writeValueAsString(schoolSubjectDto);

        mockMvc.perform(patch("/admin/teachers/1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isBadRequest());

        then(adminTeacherService).shouldHaveNoInteractions();
    }

    @Test
    void should_return_no_content_when_deleting_teacher() throws Exception {
        doNothing().when(adminTeacherService).deleteTeacher(anyLong());

        mockMvc.perform(delete("/admin/teachers/5"))
                .andExpect(status()
                        .isNoContent());
    }
}

class MockMvcConfigAdmin {

    @Bean
    ValidationErrorHandler validationErrorHandler() {
        return new ValidationErrorHandler();
    }

    @Bean
    RestExceptionHandler restExceptionHandler() {
        return new RestExceptionHandler();
    }

    @Bean
    AdminTeacherService adminTeacherService() {
        TeacherRepository teacherRepository = mock(TeacherRepository.class);
        SchoolSubjectRepository schoolSubjectRepository = mock(SchoolSubjectRepository.class);
        AppUserRepository userRepository = mock(AppUserRepository.class);
        EmailService emailService = mock(EmailService.class);
        RoleAdder roleAdder = mock(RoleAdder.class);
        TeacherMapper teacherMapper = mock(TeacherMapper.class);
        return new AdminTeacherService(teacherRepository, schoolSubjectRepository, userRepository,
                emailService, roleAdder, teacherMapper);
    }

    @Bean
    AdminTeacherController adminTeacherController(AdminTeacherService adminTeacherService) {
        return new AdminTeacherController(adminTeacherService);
    }
}