package pl.schoolmanagementsystem.schoolSubject.controller;

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
import pl.schoolmanagementsystem.common.model.SchoolSubject;
import pl.schoolmanagementsystem.common.repository.SchoolSubjectRepository;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;
import pl.schoolmanagementsystem.schoolSubject.service.AdminSubjectService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminSubjectController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MockMvcConfig2.class)
class AdminSubjectControllerTest implements Samples {

    @MockBean
    private AdminSubjectService adminSubjectService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_get_for_all_subjects() throws Exception {
        Page<SchoolSubjectDto> listOfSubjects = new PageImpl<>(List.of(schoolSubjectDto(), schoolSubjectDto()));
        String expectedResponseBody = objectMapper.writeValueAsString(listOfSubjects);
        when(adminSubjectService.getAllSubjects(any())).thenReturn(listOfSubjects);

        MvcResult mvcResult = mockMvc.perform(get("/admin/subjects"))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void should_return_status_created_when_post_for_creating_school_subject() throws Exception {
        String body = objectMapper.writeValueAsString(schoolSubjectDto());
        when(adminSubjectService.createSchoolSubject(any())).thenReturn(new SchoolSubject());

        MvcResult mvcResult = mockMvc.perform(post("/admin/subjects")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isCreated()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(body);
    }

    @Test
    void should_return_status_bad_request_when_post_for_creating_school_subject() throws Exception {
        String body = objectMapper.writeValueAsString(new SchoolSubjectDto());

        mockMvc.perform(post("/admin/subjects")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());

        then(adminSubjectService).shouldHaveNoInteractions();
    }

    @Test
    void should_return_no_content_when_deleting_school_subject() throws Exception {
        doNothing().when(adminSubjectService).deleteSchoolSubject(anyString());

        mockMvc.perform(delete("/admin/subjects/1a"))
                .andExpect(status()
                        .isNoContent());
    }

}

class MockMvcConfig2 {

    @Bean
    ValidationErrorHandler validationErrorHandler() {
        return new ValidationErrorHandler();
    }

    @Bean
    RestExceptionHandler restExceptionHandler() {
        return new RestExceptionHandler();
    }

    @Bean
    AdminSubjectService adminSubjectService() {
        SchoolSubjectRepository schoolSubjectRepository = mock(SchoolSubjectRepository.class);
        return new AdminSubjectService(schoolSubjectRepository);
    }

    @Bean
    AdminSubjectController adminSubjectController(AdminSubjectService adminSubjectService) {
        return new AdminSubjectController(adminSubjectService);
    }
}