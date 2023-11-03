package isthatkirill.tasklist.mail.service;

import isthatkirill.tasklist.mail.model.MailType;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.user.model.User;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @SpyBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MailService mailService;

    private final User user = User.builder()
            .email("email@email.ru")
            .name("name")
            .build();

    private final Task task = Task.builder()
            .title("title")
            .description("description")
            .status("DONE")
            .owner(user)
            .build();

    @Test
    void sendRegistrationEmailTest() {
        doNothing().when(javaMailSender).send((MimeMessage) any());

        mailService.sendEmail(user, MailType.REGISTRATION, null);

        verify(javaMailSender).send((MimeMessage) any());
    }

    @Test
    void sendReminderEmailTest() {
        doNothing().when(javaMailSender).send((MimeMessage) any());

        Properties properties = new Properties();
        properties.setProperty("task.title", task.getTitle());
        properties.setProperty("task.description", task.getDescription());
        properties.setProperty("task.status", task.getStatus());

        mailService.sendEmail(user, MailType.REMINDER, properties);

        verify(javaMailSender).send((MimeMessage) any());
    }
}