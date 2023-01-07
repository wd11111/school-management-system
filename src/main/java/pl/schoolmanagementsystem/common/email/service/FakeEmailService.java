package pl.schoolmanagementsystem.common.email.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeEmailService implements EmailSender {

    public void sendEmail(String receiver, String token) {
        log.info("Fake email sent to: {}", receiver);
    }

}
