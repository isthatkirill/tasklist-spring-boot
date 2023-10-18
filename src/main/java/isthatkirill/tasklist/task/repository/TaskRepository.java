package isthatkirill.tasklist.task.repository;

import isthatkirill.tasklist.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, CriteriaTaskRepository {

    boolean existsTaskByIdAndAndOwnerId(Long taskId, Long userId);

    List<Task> findTasksByIdInAndOwnerId(List<Long> ids, Long userId);

    @Query("SELECT t FROM Task t " +
            "WHERE t.notify = true " +
            "AND t.expiresAt BETWEEN CURRENT_TIMESTAMP AND :end")
    List<Task> findAllSoonTasks(@Param("end") LocalDateTime end);

}
