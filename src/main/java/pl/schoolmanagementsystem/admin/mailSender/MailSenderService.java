package pl.schoolmanagementsystem.admin.mailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderService {

    @Value("${application.url}")
    private String url;

    @Value("${spring.mail.username}}")
    private String sender;

    private final JavaMailSender mailSender;

    public void sendEmail(String receiver, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setText(url + token);
        message.setSubject("Account confirmation");
        mailSender.send(message);
        log.info("Mail send successfully to: {}", receiver);
    }
}
