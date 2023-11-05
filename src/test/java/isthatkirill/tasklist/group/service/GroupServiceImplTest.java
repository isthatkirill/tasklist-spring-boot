package isthatkirill.tasklist.group.service;

import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.group.dto.GroupDtoRequest;
import isthatkirill.tasklist.group.dto.GroupDtoResponse;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import isthatkirill.tasklist.task.model.enums.Status;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
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
class GroupServiceImplTest {

    @Autowired
    private GroupService groupService;

    @Test
    @Order(1)
    @Sql(value = {"/scripts/drop.sql", "/scripts/init.sql", "/scripts/users.sql", "/scripts/tasks.sql", "/scripts/groups.sql"})
    void createTest() {
        Long userId = 1L;
        GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
                .title("user1_group")
                .description("description")
                .taskIds(List.of(1L, 2L, 3L))
                .build();

        GroupDtoResponse groupDtoResponse = groupService.create(groupDtoRequest, userId);

        List<TaskDtoResponse> tasks = groupDtoResponse.getTasks();
        String expectedProgress = computeProgress(tasks);

        assertThat(groupDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L) // group with id=1 in groups.sql
                .hasFieldOrPropertyWithValue("title", groupDtoRequest.getTitle())
                .hasFieldOrPropertyWithValue("description", groupDtoRequest.getDescription())
                .hasFieldOrPropertyWithValue("progress", expectedProgress);

        assertThat(tasks).hasSize(3)
                .extracting(TaskDtoResponse::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @Order(2)
    void createWithNonExistentTaskTest() {
        Long userId = 1L;
        GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
                .title("user1_group")
                .description("description")
                .taskIds(List.of(1L, 2L, 3L, Long.MAX_VALUE))
                .build();

        assertThrows(EntityNotFoundException.class, () -> groupService.create(groupDtoRequest, userId));
    }

    @Test
    @Order(3)
    void createWithNonOwnTaskTest() {
        Long userId = 1L;
        GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
                .title("user1_group")
                .description("description")
                .taskIds(List.of(1L, 2L, 3L, 9L))
                .build();

        assertThrows(EntityNotFoundException.class, () -> groupService.create(groupDtoRequest, userId));
    }

    @Test
    @Order(4)
    void createEmptyTest() {
        Long userId = 1L;
        GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
                .title("user1_group_empty")
                .description("description")
                .taskIds(Collections.emptyList())
                .build();

        GroupDtoResponse groupDtoResponse = groupService.create(groupDtoRequest, userId);

        List<TaskDtoResponse> tasks = groupDtoResponse.getTasks();
        String expectedProgress = computeProgress(tasks);

        assertThat(groupDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("title", groupDtoRequest.getTitle())
                .hasFieldOrPropertyWithValue("description", groupDtoRequest.getDescription())
                .hasFieldOrPropertyWithValue("progress", expectedProgress);

        assertThat(tasks).isEmpty();
    }

    @Test
    @Order(5)
    void updateTitleAndTasksTest() {
        Long userId = 1L;
        Long groupId = 2L;
        GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
                .title("user1_group_updated")
                .taskIds(List.of(1L, 2L))
                .build();

        GroupDtoResponse groupDtoResponse = groupService.update(groupDtoRequest, groupId, userId);

        List<TaskDtoResponse> tasks = groupDtoResponse.getTasks();
        String expectedProgress = computeProgress(tasks);

        assertThat(groupDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("title", groupDtoRequest.getTitle())
                .hasFieldOrPropertyWithValue("progress", expectedProgress);

        assertThat(tasks).hasSize(2)
                .extracting(TaskDtoResponse::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @Order(6)
    void updateDescriptionTest() {
        Long userId = 1L;
        Long groupId = 2L;
        GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
                .taskIds(Collections.emptyList())
                .description("description_updated")
                .build();

        GroupDtoResponse groupDtoResponse = groupService.update(groupDtoRequest, groupId, userId);

        List<TaskDtoResponse> tasks = groupDtoResponse.getTasks();
        String expectedProgress = computeProgress(tasks);

        assertThat(groupDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("description", groupDtoRequest.getDescription())
                .hasFieldOrPropertyWithValue("progress", expectedProgress);

        assertThat(tasks).isEmpty();
    }

    @Test
    @Order(7)
    void updateAllFieldsTest() {
        Long userId = 1L;
        Long groupId = 2L;
        GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
                .title("user1_group_updated_all")
                .description("description_updated_all")
                .taskIds(List.of(1L, 2L, 3L, 4L))
                .build();

        GroupDtoResponse groupDtoResponse = groupService.update(groupDtoRequest, groupId, userId);

        List<TaskDtoResponse> tasks = groupDtoResponse.getTasks();
        String expectedProgress = computeProgress(tasks);

        assertThat(groupDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("title", groupDtoRequest.getTitle())
                .hasFieldOrPropertyWithValue("description", groupDtoRequest.getDescription())
                .hasFieldOrPropertyWithValue("progress", expectedProgress);

        assertThat(tasks).hasSize(4)
                .extracting(TaskDtoResponse::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @Test
    @Order(8)
    void addTaskInGroupTest() {
        Long userId = 1L;
        Long groupId = 2L;
        Long taskId = 5L;

        GroupDtoResponse groupDtoResponse = groupService.addTaskInGroup(userId, groupId, taskId);

        List<TaskDtoResponse> tasks = groupDtoResponse.getTasks();
        String expectedProgress = computeProgress(tasks);

        assertThat(groupDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("progress", expectedProgress);

        assertThat(tasks).hasSize(5)
                .extracting(TaskDtoResponse::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    @Order(9)
    void addTaskInNonExistentGroupTest() {
        Long userId = 1L;
        Long groupId = Long.MAX_VALUE;
        Long taskId = 5L;

        assertThrows(EntityNotFoundException.class, () -> groupService.addTaskInGroup(userId, groupId, taskId));
    }

    @Test
    @Order(10)
    void addNonExistentTaskInGroupTest() {
        Long userId = 1L;
        Long groupId = 2L;
        Long taskId = Long.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> groupService.addTaskInGroup(userId, groupId, taskId));
    }

    @Test
    @Order(11)
    void addAlreadyAddedTaskInGroupTest() {
        Long userId = 1L;
        Long groupId = 2L;
        Long taskId = 5L;

        assertThrows(IllegalStateException.class, () -> groupService.addTaskInGroup(userId, groupId, taskId));
    }

    @Test
    @Order(12)
    void deleteTaskFromGroup() {
        Long userId = 1L;
        Long groupId = 2L;
        Long taskId = 1L;

        GroupDtoResponse groupDtoResponse = groupService.deleteTaskFromGroup(userId, groupId, taskId);

        List<TaskDtoResponse> tasks = groupDtoResponse.getTasks();
        String expectedProgress = computeProgress(tasks);

        assertThat(groupDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("progress", expectedProgress);

        assertThat(tasks).hasSize(4)
                .extracting(TaskDtoResponse::getId)
                .containsExactlyInAnyOrder(2L, 3L, 4L, 5L);
    }

    @Test
    @Order(13)
    void deleteTaskFromNonExistentGroup() {
        Long userId = 1L;
        Long groupId = Long.MAX_VALUE;
        Long taskId = 2L;

        assertThrows(EntityNotFoundException.class, () -> groupService.deleteTaskFromGroup(userId, groupId, taskId));
    }

    @Test
    @Order(14)
    void deleteNonExistentTaskFromGroup() {
        Long userId = 1L;
        Long groupId = Long.MAX_VALUE;
        Long taskId = 2L;

        assertThrows(EntityNotFoundException.class, () -> groupService.deleteTaskFromGroup(userId, groupId, taskId));
    }

    @Test
    @Order(15)
    void deleteTest() {
        Long groupId = 2L;

        assertDoesNotThrow(() -> groupService.delete(groupId));
        assertThrows(EntityNotFoundException.class, () -> groupService.delete(groupId));
    }

    @Test
    @Order(16)
    void getByIdTest() {
        Long groupId = 1L;

        GroupDtoResponse groupDtoResponse = groupService.getById(groupId);

        List<TaskDtoResponse> tasks = groupDtoResponse.getTasks();

        assertThat(groupDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", groupId)
                .hasFieldOrPropertyWithValue("title", "group_title")
                .hasFieldOrPropertyWithValue("description", "group_description")
                .hasFieldOrPropertyWithValue("progress", "0/2");

        assertThat(tasks).hasSize(2)
                .extracting(TaskDtoResponse::getId)
                .containsExactlyInAnyOrder(8L, 9L);
    }

    @Test
    @Order(16)
    void getByIdNonExistentTest() {
        Long groupId = Long.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> groupService.getById(groupId));
    }

    private String computeProgress(List<TaskDtoResponse> tasks) {
        return tasks.stream()
                .filter(t -> t.getStatus().equals(Status.DONE.name()))
                .count() + "/" + tasks.size();
    }
}