package isthatkirill.tasklist.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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

}
