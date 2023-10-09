package isthatkirill.tasklist.group.repository;

import isthatkirill.tasklist.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kirill Emelyanov
 */

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    boolean existsGroupByIdAndOwnerId(Long groupId, Long userId);

}
