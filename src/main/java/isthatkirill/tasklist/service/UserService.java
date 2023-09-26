package isthatkirill.tasklist.service;

import isthatkirill.tasklist.model.User;

/**
 * @author Kirill Emelyanov
 */

public interface UserService {

    User getByUsername(String username);

    User getById(Long id);

}
