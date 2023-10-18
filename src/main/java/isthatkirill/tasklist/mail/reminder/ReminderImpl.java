package isthatkirill.tasklist.mail.reminder;

import isthatkirill.tasklist.mail.model.MailType;
import isthatkirill.tasklist.mail.service.MailService;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {

    private final TaskService taskService;
    private final MailService mailService;
    private final Duration duration = Duration.ofHours(1);

    @Override
    @Scheduled(cron = "0 * * * * *")
    public void remind() {
        List<Task> tasks = taskService.getAllSoonTasks(duration);
        tasks.forEach(task -> {
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            properties.setProperty("task.status", task.getStatus());
            mailService.sendEmail(task.getOwner(), MailType.REMINDER, properties);
        });
    }

}
