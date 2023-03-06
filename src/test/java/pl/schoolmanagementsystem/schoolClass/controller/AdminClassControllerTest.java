package pl.schoolmanagementsystem.schoolClass.controller;

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
import pl.schoolmanagementsystem.common.dto.TaughtSubjectDto;
import pl.schoolmanagementsystem.common.model.SchoolClass;
import pl.schoolmanagementsystem.common.repository.SchoolClassRepository;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.repository.StudentRepository;
import pl.schoolmanagementsystem.common.repository.TeacherInClassRepository;
import pl.schoolmanagementsystem.common.repository.TeacherRepository;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;
import pl.schoolmanagementsystem.schoolClass.dto.AddOrRemoveTeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.schoolClass.dto.StudentDto;
import pl.schoolmanagementsystem.schoolClass.dto.TeacherInClassDto;
import pl.schoolmanagementsystem.schoolClass.service.AdminClassService;
import pl.schoolmanagementsystem.schoolClass.service.AdminTeacherInClassService;
import pl.schoolmanagementsystem.schoolClass.utils.SchoolClassMapper;
import pl.schoolmanagementsystem.schoolClass.utils.TeacherInClassMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminClassController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MockMvcConfig.class)
class AdminClassControllerTest implements Samples {

    @MockBean
    private AdminClassService adminClassService;

    @MockBean
    private AdminTeacherInClassService adminTeacherInClassService;

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
        when(adminClassService.getAllStudentsInClass("1A")).thenReturn(listOfStudents);

        MvcResult mvcResult = mockMvc.perform(get("/admin/classes/1A/students"))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_ok_when_get_for_all_taught_subjects_in_class() throws Exception {
        List<TaughtSubjectDto> listOfSubjects = List.of(createTaughtSubjectDto(), createTaughtSubjectDto());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfSubjects);
        when(adminClassService.getTaughtSubjectsInClass("1A")).thenReturn(listOfSubjects);

        MvcResult mvcResult = mockMvc.perform(get("/admin/classes/1A/subjects"))
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
    void should_return_bad_request_when_post_for_classes_and_body_doesnt_pass_validation() throws Exception {
        String body = objectMapper.writeValueAsString(new SchoolClassDto());

        mockMvc.perform(post("/admin/classes")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());

        then(adminClassService).shouldHaveNoInteractions();
    }

    @Test
    void should_return_status_ok_when_assigning_teacher_to_class() throws Exception {
        TeacherInClassDto teacherInClassDto = teacherInClassResponse();
        String body = objectMapper.writeValueAsString(teacherInClassRequest());
        String expectedResponse = objectMapper.writeValueAsString(teacherInClassDto);
        when(adminTeacherInClassService.assignTeacherToSchoolClass(any(), anyString())).thenReturn(teacherInClassDto);

        MvcResult mvcResult = mockMvc.perform(post("/admin/classes/1a/teachers")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponse);
    }

    @Test
    void should_return_status_bad_request_when_assigning_teacher_to_class_and_doesnt_pass_validation() throws Exception {
        String body = objectMapper.writeValueAsString(new AddOrRemoveTeacherInClassDto());

        mockMvc.perform(post("/admin/classes/1a/teachers")
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

    SchoolClassRepository schoolClassRepository = mock(SchoolClassRepository.class);
    SchoolSubjectRepository schoolSubjectRepository = mock(SchoolSubjectRepository.class);
    StudentRepository studentRepository = mock(StudentRepository.class);
    SchoolClassMapper schoolClassMapper = mock(SchoolClassMapper.class);
    TeacherInClassRepository teacherInClassRepository = mock(TeacherInClassRepository.class);
    TeacherRepository teacherRepository = mock(TeacherRepository.class);
    TeacherInClassMapper teacherInClassMapper = mock(TeacherInClassMapper.class);

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
        return new AdminClassService(schoolClassRepository, schoolSubjectRepository, studentRepository, schoolClassMapper);
    }

    @Bean
    AdminTeacherInClassService adminTeacherInClassService(){
        return new AdminTeacherInClassService(teacherInClassRepository, schoolClassRepository, teacherRepository, schoolSubjectRepository, teacherInClassMapper);
    }

    @Bean
    AdminClassController AdminClassController(AdminClassService adminClassService, AdminTeacherInClassService adminTeacherInClassService) {
        return new AdminClassController(adminClassService, adminTeacherInClassService);
    }
}