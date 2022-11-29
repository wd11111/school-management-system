package pl.schoolmanagementsystem.admin.teacher.controller;

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
import pl.schoolmanagementsystem.ControllerSamples;
import pl.schoolmanagementsystem.admin.mailSender.MailSenderService;
import pl.schoolmanagementsystem.admin.teacher.mapper.TeacherCreator;
import pl.schoolmanagementsystem.admin.teacher.service.AdminTeacherService;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SchoolSubjectDto;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndClassDto;
import pl.schoolmanagementsystem.common.teacher.Teacher;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherInputDto;
import pl.schoolmanagementsystem.common.teacher.dto.TeacherOutputDto;
import pl.schoolmanagementsystem.common.user.AppUserRepository;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminTeacherController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MockMvcConfig4.class)
class AdminTeacherControllerTest implements ControllerSamples {

    @MockBean
    private AdminTeacherService adminTeacherService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_get_for_all_teachers() throws Exception {
        Page<Teacher> listOfTeachers = new PageImpl<>(List.of(createTeacherOfBiology(), createTeacherOfBiology()));
        Page<TeacherOutputDto> listOfTeachersOutput = new PageImpl<>(List.of(teacherOutputDto(), teacherOutputDto()));
        String expectedResponseBody = objectMapper.writeValueAsString(listOfTeachersOutput);
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
        when(adminTeacherService.getTaughtClassesByTeacher(anyInt(), any())).thenReturn(listOfTaughtClasses);

        MvcResult mvcResult = mockMvc.perform(get("/admin/teachers/3/taught-classes"))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_created_when_creating_teacher() throws Exception {
        TeacherInputDto teacherInputDto = teacherInputDto();
        Teacher teacher = createTeacherNoSubjectsTaught();
        String body = objectMapper.writeValueAsString(teacherInputDto);
        when(adminTeacherService.createTeacher(any())).thenReturn(teacher);

        MvcResult mvcResult = mockMvc.perform(post("/admin/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isCreated())
                .andReturn();
        Teacher actualResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Teacher.class);

        assertThat(actualResponseBody.getName()).isEqualTo(teacherInputDto.getName());
    }

    @Test
    void should_return_status_bad_request_when_creating_teacher() throws Exception {
        TeacherInputDto teacherInputDto = new TeacherInputDto();
        String body = objectMapper.writeValueAsString(teacherInputDto);

        mockMvc.perform(post("/admin/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status()
                        .isBadRequest());

        then(adminTeacherService).shouldHaveNoInteractions();
    }

    @Test
    void should_return_status_ok_when_adding_subject_to_teacher() throws Exception {
        SchoolSubjectDto schoolSubjectDto = schoolSubjectDto();
        Teacher teacher = createTeacherOfBiology();
        TeacherOutputDto teacherOutputDto = teacherOutputDto();
        String expectedResponse = objectMapper.writeValueAsString(teacherOutputDto);
        String body = objectMapper.writeValueAsString(schoolSubjectDto);
        when(adminTeacherService.addSubjectToTeacher(anyInt(), any())).thenReturn(teacher);

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
    void should_return_status_bad_request_when_adding_subject_to_teacher() throws Exception {
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
        doNothing().when(adminTeacherService).deleteTeacher(anyInt());

        mockMvc.perform(delete("/admin/teachers/5"))
                .andExpect(status()
                        .isNoContent());
    }
}

class MockMvcConfig4 {

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
        MailSenderService mailSenderService = mock(MailSenderService.class);
        TeacherCreator teacherCreator = mock(TeacherCreator.class);
        return new AdminTeacherService(teacherRepository, schoolSubjectRepository, userRepository,
                mailSenderService, teacherCreator);
    }

    @Bean
    AdminTeacherController adminTeacherController(AdminTeacherService adminTeacherService) {
        return new AdminTeacherController(adminTeacherService);
    }
}