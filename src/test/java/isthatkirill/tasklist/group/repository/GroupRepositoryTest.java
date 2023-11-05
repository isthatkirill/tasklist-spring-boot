package isthatkirill.tasklist.group.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kirill Emelyanov
 */
@ActiveProfiles("test")
@DataJpaTest
@Sql(value = {"/scripts/drop.sql", "/scripts/init.sql", "/scripts/users.sql", "/scripts/tasks.sql", "/scripts/groups.sql"})
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void existsGroupByIdAndOwnerIdTest() {
        Long userId = 2L;
        Long groupId = 1L;

        boolean isExists = groupRepository.existsGroupByIdAndOwnerId(groupId, userId);

        assertThat(isExists).isTrue();
    }

}