package isthatkirill.tasklist.mail.reminder;

import isthatkirill.tasklist.mail.service.MailService;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.task.service.TaskService;
import isthatkirill.tasklist.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReminderImplTest {

    @MockBean
    private TaskService taskService;

    @MockBean
    private MailService mailService;

    private final Task task = Task.builder()
            .title("title")
            .description("description")
            .status("DONE")
            .owner(User.builder().id(1L).build())
            .build();

    @Autowired
    private Reminder reminder;

    @Test
    void remindTest() {
        when(taskService.getAllSoonTasks(any())).thenReturn(List.of(task));
        Properties properties = new Properties();
        properties.setProperty("task.title", task.getTitle());
        properties.setProperty("task.description", task.getDescription());
        properties.setProperty("task.status", task.getStatus());

        reminder.remind();

        verify(mailService).sendEmail(eq(task.getOwner()), any(), eq(properties));
    }
}