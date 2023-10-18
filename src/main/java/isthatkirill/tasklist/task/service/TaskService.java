package isthatkirill.tasklist.task.service;

import isthatkirill.tasklist.task.dto.TaskDtoRequest;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import isthatkirill.tasklist.task.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface TaskService {

    TaskDtoResponse create(TaskDtoRequest taskDto, Long userId);

    TaskDtoResponse update(TaskDtoRequest taskDto, Long userId, Long taskId);

    TaskDtoResponse getById(Long taskId);

    void delete(Long taskId);

    List<TaskDtoResponse> getAll(Long userId, String keyword, String priority, String status, LocalDateTime expiresBefore,
                                 Boolean notify, Integer from, Integer size);

    List<Task> getAllSoonTasks(Duration duration);

}
