package pl.schoolmanagementsystem.email.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import pl.schoolmanagementsystem.email.service.EmailService;
import pl.schoolmanagementsystem.email.service.FakeEmailService;

@Configuration
public class EmailConfig {

    @Bean
    @ConditionalOnProperty(name = "use.real.email.sender", havingValue = "true")
    public EmailService emailService(JavaMailSender mailSender) {
        return new EmailService(mailSender);
    }

    @Bean
    @ConditionalOnProperty(name = "use.real.email.sender", havingValue = "false")
    public FakeEmailService fakeEmailService() {
        return new FakeEmailService();
    }
}
