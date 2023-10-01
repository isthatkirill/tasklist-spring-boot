package isthatkirill.tasklist.task.repository;

import isthatkirill.tasklist.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kirill Emelyanov
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, CriteriaTaskRepository {

    boolean existsTaskByIdAndAndOwnerId(Long taskId, Long userId);

}
