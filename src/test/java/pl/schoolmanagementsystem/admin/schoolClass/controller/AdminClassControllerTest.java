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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.schoolmanagementsystem.ControllerSamples;
import pl.schoolmanagementsystem.admin.schoolClass.service.AdminClassService;
import pl.schoolmanagementsystem.admin.schoolClass.service.AdminTeacherInClassService;
import pl.schoolmanagementsystem.common.schoolClass.SchoolClassRepository;
import pl.schoolmanagementsystem.common.schoolClass.dto.SchoolClassDto;
import pl.schoolmanagementsystem.common.schoolSubject.SchoolSubjectRepository;
import pl.schoolmanagementsystem.common.schoolSubject.dto.SubjectAndTeacherOutputDto;
import pl.schoolmanagementsystem.common.student.StudentRepository;
import pl.schoolmanagementsystem.common.student.dto.StudentOutputDto2;
import pl.schoolmanagementsystem.common.teacher.TeacherRepository;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void should_return_status_ok_when_get_for_school_classes() throws Exception {
        Page<SchoolClassDto> listOfClasses = new PageImpl<>(List.of(schoolClassDto(), schoolClassDto()));
        String expectedResponseBody = objectMapper.writeValueAsString(listOfClasses);
        when(adminClassService.getSchoolClasses(any())).thenReturn(listOfClasses);

        MvcResult mvcResult = mockMvc.perform(get("/admin/classes"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void should_return_status_ok_when_get_for_all_students_in_class() throws Exception {
        List<StudentOutputDto2> listOfStudents = List.of(studentOutputDto2(), studentOutputDto2());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfStudents);
        when(adminClassService.getAllStudentsInClass("1a")).thenReturn(listOfStudents);

        MvcResult mvcResult = mockMvc.perform(get("/admin/classes/1a"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    public void should_return_status_ok_when_get_for_all_taught_subjects_in_class() throws Exception {
        List<SubjectAndTeacherOutputDto> listOfSubjects = List.of(subjectAndTeacherOutput(), subjectAndTeacherOutput());
        String expectedResponseBody = objectMapper.writeValueAsString(listOfSubjects);
        when(adminClassService.getAllSubjectsInSchoolClass("1a")).thenReturn(listOfSubjects);

        MvcResult mvcResult = mockMvc.perform(get("/admin/classes/1a/subjects"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }


}

class MockMvcConfig {

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