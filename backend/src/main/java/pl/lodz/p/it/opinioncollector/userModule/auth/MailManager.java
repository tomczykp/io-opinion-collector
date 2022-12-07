package pl.lodz.p.it.opinioncollector.userModule.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
public class MailManager {

    private final JavaMailSender mailSender;


    @Async
    private void send(String to, String name, String message, String last, String action, String link) {
        try {
            File registrationEmail = new File("src/main/resources/html/template.html");
            String email = FileUtils.readFileToString(registrationEmail);
            email = email.replace("$username", name);
            email = email.replace("$link", link);
            email = email.replace("$message", message);
            email = email.replace("$last", last);
            email = email.replace("$action", action);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("io.opinioncollector@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registrationEmail(String to, String name, String link) {
        this.send(to,
                name,
                "Thank you for joining Opinion Collector. Please click on the below link to activate your account:",
                "See you soon",
                "Activate Now",
                link);
    }

    public void deletionEmail(String to, String name, String link) {
        this.send(to,
                name,
                "We are sad that you want to leave us already. If you really want to go, click on the link to activate your account:",
                "Hope to see you soon",
                "Delete Account",
                link);
    }

    public void adminActionEmail(String to, String name, String action) {
        this.send(to,
                name,
                "We would like to report that Administrator has " + action + ",your accout.",
                "See you soon",
                "",
                "");
    }
}
