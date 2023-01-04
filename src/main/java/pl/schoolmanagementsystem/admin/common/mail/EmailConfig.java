package pl.schoolmanagementsystem.admin.common.mail;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

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
