package pl.schoolmanagementsystem.admin.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${application.url}")
    private String applicationUrl;

    @Value("${spring.mail.username}}")
    private String sender;

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String receiver, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setText(applicationUrl + token);
        message.setSubject("Account confirmation");
        mailSender.send(message);
        log.info("Mail send successfully to: {}", receiver);
    }
}
