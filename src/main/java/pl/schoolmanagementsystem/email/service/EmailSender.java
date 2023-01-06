package pl.schoolmanagementsystem.email.service;

public interface EmailSender {

    void sendEmail(String receiver, String token);
}
