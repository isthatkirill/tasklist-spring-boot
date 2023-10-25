package isthatkirill.tasklist.task.service;

import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.task.dto.TaskDtoRequest;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import isthatkirill.tasklist.task.model.Task;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    private final TaskDtoRequest taskDtoRequest = TaskDtoRequest.builder()
            .title("title")
            .description("description")
            .status("NEW")
            .priority("LOW")
            .notify(true)
            .expiresAt(LocalDateTime.now().plusHours(2))
            .build();

    @Test
    @Order(1)
    @Sql(value = {"/scripts/drop.sql", "/scripts/init.sql", "/scripts/users.sql", "/scripts/tasks.sql"})
    void createTest() {
        Long userId = 2L;

        TaskDtoResponse taskDtoResponse = taskService.create(taskDtoRequest, userId);

        assertThat(taskDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 10L) // id=10, previous in tasks.sql
                .hasFieldOrPropertyWithValue("title", taskDtoRequest.getTitle())
                .hasFieldOrPropertyWithValue("description", taskDtoRequest.getDescription())
                .hasFieldOrPropertyWithValue("status", taskDtoRequest.getStatus())
                .hasFieldOrPropertyWithValue("priority", taskDtoRequest.getPriority())
                .hasFieldOrPropertyWithValue("notify", taskDtoRequest.getNotify())
                .hasFieldOrPropertyWithValue("expiresAt", taskDtoRequest.getExpiresAt())
                .hasFieldOrProperty("createdAt")
                .hasFieldOrPropertyWithValue("lastModifiedAt", null);
    }

    @Test
    @Order(2)
    void createByNonExistentTest() {
        Long userId = Long.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> taskService.create(taskDtoRequest, userId));
    }

    @Test
    @Order(3)
    void updateWithoutAnyChangesTest() {
        Long userId = 2L;
        Long taskId = 10L;
        TaskDtoRequest update = new TaskDtoRequest();

        TaskDtoResponse taskDtoResponse = taskService.update(update, userId, taskId);

        assertThat(taskDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("title", taskDtoRequest.getTitle())
                .hasFieldOrPropertyWithValue("description", taskDtoRequest.getDescription())
                .hasFieldOrPropertyWithValue("status", taskDtoRequest.getStatus())
                .hasFieldOrPropertyWithValue("priority", taskDtoRequest.getPriority())
                .hasFieldOrPropertyWithValue("notify", taskDtoRequest.getNotify())
                .hasFieldOrProperty("expiresAt")
                .hasFieldOrProperty("createdAt")
                .hasFieldOrProperty("lastModifiedAt");
    }

    @Test
    @Order(4)
    void updateWithAllFieldsTest() {
        Long userId = 2L;
        Long taskId = 10L;
        TaskDtoRequest update = TaskDtoRequest.builder()
                .title("title_updated")
                .description("description_updated")
                .status("DONE")
                .priority("HIGH")
                .notify(false)
                .expiresAt(LocalDateTime.now().plusHours(3))
                .build();

        TaskDtoResponse taskDtoResponse = taskService.update(update, userId, taskId);

        assertThat(taskDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("title", update.getTitle())
                .hasFieldOrPropertyWithValue("description", update.getDescription())
                .hasFieldOrPropertyWithValue("status", update.getStatus())
                .hasFieldOrPropertyWithValue("priority", update.getPriority())
                .hasFieldOrPropertyWithValue("notify", update.getNotify())
                .hasFieldOrProperty("expiresAt")
                .hasFieldOrProperty("createdAt")
                .hasFieldOrProperty("lastModifiedAt");
    }

    @Test
    @Order(5)
    void updateNonExistentTaskTest() {
        Long userId = 2L;
        Long taskId = Long.MAX_VALUE;
        TaskDtoRequest update = new TaskDtoRequest();

        assertThrows(EntityNotFoundException.class, () -> taskService.update(update, userId, taskId));
    }

    @Test
    @Order(6)
    void getByIdTest() {
        Long taskId = 1L;

        TaskDtoResponse taskDtoResponse = taskService.getById(taskId);

        assertThat(taskDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("title", "Buy the car")
                .hasFieldOrPropertyWithValue("description", "Buy a mercedes-benz")
                .hasFieldOrPropertyWithValue("status", "DONE")
                .hasFieldOrPropertyWithValue("priority", "HIGH")
                .hasFieldOrPropertyWithValue("notify", false)
                .hasFieldOrProperty("expiresAt")
                .hasFieldOrProperty("createdAt");
    }

    @Test
    @Order(7)
    void getNonExistentByIdTest() {
        Long taskId = Long.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> taskService.getById(taskId));
    }

    @Test
    @Order(8)
    void delete() {
        Long taskId = 10L;

        assertDoesNotThrow(() -> taskService.delete(taskId));
        assertThrows(EntityNotFoundException.class, () -> taskService.getById(taskId));
    }

    @Test
    @Order(9)
    void getAllSoonTasks() {
        Duration duration = Duration.ofDays(3);

        List<Task> tasks = taskService.getAllSoonTasks(duration);

        assertThat(tasks).hasSize(4) // findAllSoonTasks() also filters by notify = true, so
                .extracting(Task::getId)   // correct list size is 4
                .containsExactlyInAnyOrder(3L, 5L, 8L, 9L);
    }

    @Test
    @Order(10)
    void getAllWithoutAnyParamsTest() {
        Long userId = 1L;
        Integer defaultFrom = 0;
        Integer defaultSize = 10;

        List<TaskDtoResponse> tasks = taskService.getAll(userId, null, null, null, null, null, defaultFrom, defaultSize);

        assertThat(tasks).hasSize(7)
                .extracting(TaskDtoResponse::getId)
                .containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    }

    @Test
    @Order(11)
    void getAllWithAllParamsTest() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 3;
        String keyword = "buy";
        String priority = "LOW";
        String status = "DONE";
        Boolean notify = false;
        LocalDateTime expiresBefore = LocalDateTime.now().plusYears(1);

        List<TaskDtoResponse> tasks = taskService.getAll(userId, keyword, priority, status, expiresBefore, notify, from, size);

        assertThat(tasks).hasSize(1)
                .extracting(TaskDtoResponse::getId)
                .containsExactly(6L);
    }

}