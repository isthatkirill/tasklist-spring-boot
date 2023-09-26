package isthatkirill.tasklist.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.config.Task;

import java.util.List;
import java.util.Set;

/**
 * @author Kirill Emelyanov
 */

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;
    String name;
    String username;
    String email;
    String password;
    Set<Role> roles;
    List<Task> tasks;

}
