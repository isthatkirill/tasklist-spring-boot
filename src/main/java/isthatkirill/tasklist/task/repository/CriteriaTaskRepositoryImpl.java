package isthatkirill.tasklist.task.repository;

import isthatkirill.tasklist.task.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public class CriteriaTaskRepositoryImpl implements CriteriaTaskRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Task> getAllTasks(Long userId, String keyword, String priority, String status, LocalDateTime expiresBefore,
                                  Boolean notify, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);
        Predicate predicate = cb.conjunction();

        predicate = cb.and(predicate, root.get("owner").in(userId));

        if (keyword != null && !keyword.isBlank()) {
            Predicate title = cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
            Predicate description = cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
            predicate = cb.and(predicate, cb.or(title, description));
        }

        if (priority != null && !priority.isBlank()) {
            predicate = cb.and(predicate, root.get("priority").in(priority));
        }
        if (status != null && !status.isBlank()) {
            predicate = cb.and(predicate, root.get("status").in(status));
        }
        if (expiresBefore != null) {
            predicate = cb.and(predicate, cb.greaterThan(root.get("expiresAt"), expiresBefore).not());
        }
        if (notify != null) {
            predicate = cb.and(predicate, root.get("notify").in(notify));
        }

        query.select(root).where(predicate);
        return entityManager
                .createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }


}
