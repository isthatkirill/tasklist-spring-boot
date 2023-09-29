package isthatkirill.tasklist.task.service;

import isthatkirill.tasklist.task.dto.TaskDtoRequest;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;

/**
 * @author Kirill Emelyanov
 */

public interface TaskService {

    TaskDtoResponse create(TaskDtoRequest taskDto, Long userId);

}
