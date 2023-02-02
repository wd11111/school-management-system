package pl.schoolmanagementsystem.common.email.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import pl.schoolmanagementsystem.common.email.service.EmailSender;
import pl.schoolmanagementsystem.common.email.service.EmailService;
import pl.schoolmanagementsystem.common.email.service.FakeEmailService;

@Configuration
public class EmailConfig {

    @Bean
    @ConditionalOnProperty(name = "use.real.email.sender", havingValue = "true")
    public EmailSender emailService(JavaMailSender mailSender) {
        return new EmailService(mailSender);
    }

    @Bean
    @ConditionalOnProperty(name = "use.real.email.sender", havingValue = "false")
    public EmailSender fakeEmailService() {
        return new FakeEmailService();
    }

}
