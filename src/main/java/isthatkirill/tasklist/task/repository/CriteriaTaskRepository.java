package isthatkirill.tasklist.task.repository;

import isthatkirill.tasklist.task.model.Task;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface CriteriaTaskRepository {

    List<Task> getAllTasks(Long userId, String keyword, String priority, String status, LocalDateTime expiresBefore,
                           Boolean notify, Integer from, Integer size);


}
