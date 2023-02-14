package pl.schoolmanagementsystem.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.schoolmanagementsystem.common.email.service.EmailSender;
import pl.schoolmanagementsystem.common.repository.AppUserRepository;
import pl.schoolmanagementsystem.exception.RestExceptionHandler;
import pl.schoolmanagementsystem.exception.ValidationErrorHandler;
import pl.schoolmanagementsystem.security.dto.EmailDto;
import pl.schoolmanagementsystem.security.dto.PasswordDto;
import pl.schoolmanagementsystem.security.service.AccountService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = MockMvcConfig.class)
class AccountControllerTest {

    public static final String PASSWORD = "password";
    public static final String NOT_MATCHING_PASSWORD = "badPassword";

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_post_for_confirming_password() throws Exception {
        PasswordDto passwordDto = new PasswordDto(PASSWORD, PASSWORD);
        String requestBody = objectMapper.writeValueAsString(passwordDto);
        doNothing().when(accountService).confirmPassword(any(), anyString());

        mockMvc.perform(post("/account/confirm/123")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON))
                .andExpect(status()
                        .isOk());

        verify(accountService, times(1)).confirmPassword(any(), anyString());
    }

    @Test
    void should_return_bad_request_when_post_for_confirming_password_with_not_matching_passwords() throws Exception {
        PasswordDto passwordDto = new PasswordDto(PASSWORD, NOT_MATCHING_PASSWORD);
        String expectedResponse = "{\"passwordDto\":\"Passwords do not match!\"}";
        String requestBody = objectMapper.writeValueAsString(passwordDto);
        doNothing().when(accountService).confirmPassword(any(), anyString());

        MvcResult mvcResult = mockMvc.perform(post("/account/confirm/123")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        assertThat(responseBody).isEqualTo(expectedResponse);
        verify(accountService, never()).confirmPassword(any(), anyString());
    }

    @Test
    void should_return_status_ok_when_post_for_reset_password() throws Exception {
        EmailDto emailDto = new EmailDto("exampleForTest@gmail.com");
        String requestBody = objectMapper.writeValueAsString(emailDto);
        doNothing().when(accountService).resetPassword(anyString());

        mockMvc.perform(post("/account/reset-password")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON))
                .andExpect(status()
                        .isOk());

        verify(accountService, times(1)).resetPassword(anyString());
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
    AccountService accountService() {
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AppUserRepository userRepository = mock(AppUserRepository.class);
        EmailSender emailSender = mock(EmailSender.class);
        return new AccountService(passwordEncoder, userRepository, emailSender);
    }

    @Bean
    AccountController accountController(AccountService accountService) {
        return new AccountController(accountService);
    }
}