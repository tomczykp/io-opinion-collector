package pl.lodz.p.it.opinioncollector.userModule.auth;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.io.Reader;


@Service
@RequiredArgsConstructor
@Slf4j
public class MailManager {

    private final JavaMailSender mailSender;

    @Async
    void send(String to, String name, String message, String last, String action, String link, String subject, String title) {
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:html/template.html");
            Reader reader = new InputStreamReader(resource.getInputStream());

            String email = FileCopyUtils.copyToString(reader);
            email = email
                    .replace("$username", name)
                    .replace("$link", link)
                    .replace("$message", message)
                    .replace("$last", last)
                    .replace("$action", action)
                    .replace("$title", title);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("io.opinioncollector@gmail.com");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send email", e);
        }
    }

    @Async
    public void registrationEmail(String to, String name, String link) {
        this.send(to,
                name,
                "Thank you for joining Opinion Collector. Please click on the below link to activate your account:",
                "See you soon",
                "Activate Now",
                link,
                "Confirm your email",
                "Confirm your email");
    }

    @Async
    public void deletionEmail(String to, String name, String link) {
        this.send(to,
                name,
                "We are sad to see you leave us already. If you really want to go, click on the link to delete your account:",
                "Hope to see you soon",
                "Delete Account",
                link,
                "Confirm account deletion",
                "Confirm account deletion");
    }

    @Async
    public void adminActionEmail(String to, String name, String action) {
        this.send(to,
                name,
                "We would like to report that Administrator has " + action + " your account.",
                "Opinion Collector team.",
                "",
                "",
                "Administrator action",
                "Administrator action");
    }

    @Async
    public void resetPasswordEmail(String to, String name, String link) {
        this.send(to,
                name,
                "Click the link below to reset your password.",
                "Opinion Collector team.",
                "Reset your password",
                link,
                "Reset password",
                "Reset password");
    }
}
