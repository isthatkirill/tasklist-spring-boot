package isthatkirill.tasklist.user.repositorty;

import isthatkirill.tasklist.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@DataJpaTest
@Sql(value = {"/scripts/drop.sql", "/scripts/init.sql", "/scripts/users.sql"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsernameTest() {
        String username = "isthatkirill";

        Optional<User> optionalUser = userRepository.findByUsername(username);

        assertThat(optionalUser).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", username)
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void findByEmailTest() {
        String email = "annushka@yahoo.com";

        Optional<User> optionalUser = userRepository.findByEmail(email);

        assertThat(optionalUser).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("id", 2L);
    }

}