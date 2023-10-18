package isthatkirill.tasklist.task.repository;

import isthatkirill.tasklist.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, CriteriaTaskRepository {

    boolean existsTaskByIdAndAndOwnerId(Long taskId, Long userId);

    List<Task> findTasksByIdInAndOwnerId(List<Long> ids, Long userId);

    @Query(value = """
            SELECT * FROM tasks t
            WHERE t.notify is true
            AND t.expires_at between NOW() and :end
            """, nativeQuery = true)
    List<Task> findAllSoonTasks(@Param("end") Timestamp end);

}
