package isthatkirill.tasklist.mail.service;

import freemarker.template.Configuration;
import isthatkirill.tasklist.mail.model.MailType;
import isthatkirill.tasklist.user.model.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Kirill Emelyanov
 */

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(User user, MailType type, Properties params) {
        switch (type) {
            case REGISTRATION -> sendRegistrationEmail(user);
            case REMINDER -> sendReminderEmail(user, params);
            default -> {
            }
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(User user) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setSubject("Hello, " + user.getName() + ". You are successfully registered!");
        helper.setTo(user.getEmail());
        String emailContent = getRegistrationEmailContent(user);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void sendReminderEmail(User user, Properties params) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setSubject("Your task is expires in 1 hour.");
        helper.setTo(user.getEmail());
        String emailContent = getReminderEmailContent(user, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(User user) {
        StringWriter writer = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        configuration.getTemplate("register.ftlh")
                .process(map, writer);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getReminderEmailContent(User user, Properties params) {
        StringWriter writer = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("title", params.getProperty("task.title"));
        map.put("description", params.getProperty("task.description"));
        map.put("status", params.getProperty("task.status"));
        configuration.getTemplate("reminder.ftlh")
                .process(map, writer);
        return writer.getBuffer().toString();
    }

}
