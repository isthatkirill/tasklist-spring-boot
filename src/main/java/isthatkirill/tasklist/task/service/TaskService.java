package isthatkirill.tasklist.task.service;

import isthatkirill.tasklist.task.dto.UserTaskDto;

/**
 * @author Kirill Emelyanov
 */

public interface TaskService {

    UserTaskDto create(UserTaskDto taskDto, Long userId);

}
