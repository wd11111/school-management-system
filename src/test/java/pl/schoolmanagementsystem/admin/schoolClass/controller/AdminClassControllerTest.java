package pl.schoolmanagementsystem.admin.schoolClass.controller;

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
import pl.schoolmanagementsystem.common.schoolClass.SchoolClass;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.student.dto.StudentDto;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;
import pl.schoolmanagementsystem.schoolClass.controller.AdminClassController;
import pl.schoolmanagementsystem.schoolClass.dto.AddTeacherToClassDto;
import pl.schoolmanagementsystem.schoolClass.service.AdminClassService;
import pl.schoolmanagementsystem.schoolClass.service.AdminTeacherInClassService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminClassController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MockMvcConfig.class)
class AdminClassControllerTest implements ControllerSamples {

    @MockBean
    private AdminClassService adminClassService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_get_for_school_classes() throws Exception {
        Page<SchoolClassDto> listOfClasses = new PageImpl<>(List.of(schoolClassDto(), schoolClassDto()));
        String expectedResponseBody = objectMapper.writeValueAsString(listOfClasses);
        when(adminClassService.getSchoolClasses(any())).thenReturn(listOfClasses);

        MvcResult mvcResult = mockMvc.perform(get("/admin/classes"))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_ok_when_get_for_all_students_in_class() throws Exception {
        List<StudentDto> listOfStudents = List.of(studentResponseDto2(), studentResponseDto2());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfStudents);
        when(adminClassService.getAllStudentsInClass("1a")).thenReturn(listOfStudents);

        MvcResult mvcResult = mockMvc.perform(get("/admin/classes/1a"))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_ok_when_get_for_all_taught_subjects_in_class() throws Exception {
        List<TaughtSubjectDto> listOfSubjects = List.of(subjectAndTeacherResponse(), subjectAndTeacherResponse());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfSubjects);
        when(adminClassService.getTaughtSubjectsInClass("1a")).thenReturn(listOfSubjects);

        MvcResult mvcResult = mockMvc.perform(get("/admin/classes/1a/subjects"))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_created_when_post_for_creating_school_class() throws Exception {
        String body = objectMapper.writeValueAsString(schoolClassDto());
        when(adminClassService.createSchoolClass(any())).thenReturn(new SchoolClass());

        MvcResult mvcResult = mockMvc.perform(post("/admin/classes")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isCreated()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(body);
    }

    @Test
    void should_return_status_bad_request_when_body_doesnt_pass_validation() throws Exception {
        String body = objectMapper.writeValueAsString(new SchoolClassDto());

        mockMvc.perform(post("/admin/classes")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());

        then(adminClassService).shouldHaveNoInteractions();
    }

    @Test
    void should_return_status_ok_when_adding_subject_to_class() throws Exception {
        String body = objectMapper.writeValueAsString(teacherInClassRequest());
        String expectedResponse = objectMapper.writeValueAsString(teacherInClassResponse());
        when(adminClassService.addTeacherToSchoolClass(any(), anyString())).thenReturn(teacherInClass());

        MvcResult mvcResult = mockMvc.perform(patch("/admin/classes/1a/teachers")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponse);
    }

    @Test
    void should_return_status_bad_request_when_adding_subject_to_class_doesnt_pass_validation() throws Exception {
        String body = objectMapper.writeValueAsString(new AddTeacherToClassDto());

        mockMvc.perform(patch("/admin/classes/1a/teachers")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());

        then(adminClassService).shouldHaveNoInteractions();
    }

    @Test
    void should_return_no_content_when_deleting_class() throws Exception {
        doNothing().when(adminClassService).deleteSchoolClass(anyString());

        mockMvc.perform(delete("/admin/classes/1a"))
                .andExpect(status()
                        .isNoContent());
    }
}

class MockMvcConfig {

    @Bean
    ValidationErrorHandler validationErrorHandler() {
        return new ValidationErrorHandler();
    }

    @Bean
    RestExceptionHandler restExceptionHandler() {
        return new RestExceptionHandler();
    }

    @Bean
    AdminClassService adminClassService() {
        SchoolClassRepository schoolClassRepository = mock(SchoolClassRepository.class);
        TeacherRepository teacherRepository = mock(TeacherRepository.class);
        SchoolSubjectRepository schoolSubjectRepository = mock(SchoolSubjectRepository.class);
        AdminTeacherInClassService teacherInClassService = mock(AdminTeacherInClassService.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        return new AdminClassService(schoolClassRepository, teacherRepository,
                schoolSubjectRepository, teacherInClassService, studentRepository);
    }

    @Bean
    AdminClassController AdminClassController(AdminClassService adminClassService) {
        return new AdminClassController(adminClassService);
    }
}