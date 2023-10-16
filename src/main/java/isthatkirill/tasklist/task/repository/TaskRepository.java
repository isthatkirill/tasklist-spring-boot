package isthatkirill.tasklist.task.repository;

import isthatkirill.tasklist.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, CriteriaTaskRepository {

    boolean existsTaskByIdAndAndOwnerId(Long taskId, Long userId);

    List<Task> findTasksByIdInAndOwnerId(List<Long> ids, Long userId);

}
