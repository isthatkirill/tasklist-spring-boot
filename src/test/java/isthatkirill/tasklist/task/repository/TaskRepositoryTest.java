package isthatkirill.tasklist.task.repository;

import isthatkirill.tasklist.task.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@DataJpaTest
@Sql(value = {"/scripts/drop.sql", "/scripts/init.sql", "/scripts/users.sql", "/scripts/tasks.sql"})
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void existsTaskByIdAndAndOwnerIdTest() {
        Long userId = 1L;
        Long taskId = 1L;

        boolean ifExists = taskRepository.existsTaskByIdAndAndOwnerId(taskId, userId);

        assertThat(ifExists).isTrue();
    }

    @Test
    void existsTaskByIdAndAndOwnerIdNotOwnTaskTest() {
        Long userId = 2L;
        Long taskId = 1L;

        boolean ifExists = taskRepository.existsTaskByIdAndAndOwnerId(taskId, userId);

        assertThat(ifExists).isFalse();
    }

    @Test
    void findTasksByIdInAndOwnerIdTest() {
        List<Long> ids = List.of(1L, 2L, 100L);
        Long userId = 1L;

        List<Task> tasks = taskRepository.findTasksByIdInAndOwnerId(ids, userId);

        assertThat(tasks).hasSize(2)
                .extracting(Task::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void findAllSoonTasksTest() {
        List<Task> tasks = taskRepository.findAllSoonTasks(LocalDateTime.now().plusYears(100));

        assertThat(tasks).hasSize(4) // findAllSoonTasks() also filters by notify = true, so
                .extracting(Task::getId)   // correct list size is 4
                .containsExactlyInAnyOrder(3L, 5L, 8L, 9L);
    }

    @Test
    void getAllTasksWithoutAnyParamsTest() {
        Long userId = 1L;
        Integer defaultFrom = 0;
        Integer defaultSize = 10;

        List<Task> tasks = taskRepository.getAllTasks(userId, null, null, null, null, null, defaultFrom, defaultSize);

        assertThat(tasks).hasSize(7)
                .extracting(Task::getId)
                .containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    }

    @Test
    void getAllTasksWithKeywordTest() {
        Long userId = 1L;
        Integer defaultFrom = 0;
        Integer defaultSize = 10;
        String keyword = "buy";

        List<Task> tasks = taskRepository.getAllTasks(userId, keyword, null, null, null, null, defaultFrom, defaultSize);

        assertThat(tasks).hasSize(2)
                .extracting(Task::getId)
                .containsExactly(1L, 6L);
    }

    @Test
    void getAllTasksByPriorityAndStatusTest() {
        Long userId = 1L;
        Integer defaultFrom = 0;
        Integer defaultSize = 10;
        String priority = "LOW";
        String status = "DONE";

        List<Task> tasks = taskRepository.getAllTasks(userId, null, priority, status, null, null, defaultFrom, defaultSize);

        assertThat(tasks).hasSize(2)
                .extracting(Task::getId)
                .containsExactly(3L, 6L);
    }

    @Test
    void getAllTasksWithFromAndSizeTest() {
        Long userId = 1L;
        Integer from = 2;
        Integer size = 3;

        List<Task> tasks = taskRepository.getAllTasks(userId, null, null, null, null, null, from, size);

        assertThat(tasks).hasSize(3)
                .extracting(Task::getId)
                .containsExactly(3L, 4L, 5L);
    }

    @Test
    void getAllTasksWithAllParamsTest() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 3;
        String keyword = "th";
        String priority = "HIGH";
        String status = "IN_PROGRESS";
        Boolean notify = true;
        LocalDateTime expiresBefore = LocalDateTime.now().plusYears(1);

        List<Task> tasks = taskRepository.getAllTasks(userId, keyword, priority, status, expiresBefore, notify, from, size);

        assertThat(tasks).hasSize(1)
                .extracting(Task::getId)
                .containsExactly(5L);
    }

}