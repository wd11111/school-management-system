package pl.schoolmanagementsystem.admin.common.mail;

public interface EmailSender {

    void sendEmail(String receiver, String token);
}
