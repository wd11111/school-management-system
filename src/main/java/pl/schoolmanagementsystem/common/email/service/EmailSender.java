package pl.schoolmanagementsystem.common.email.service;

public interface EmailSender {

    void sendEmail(String receiver, String token);
}
