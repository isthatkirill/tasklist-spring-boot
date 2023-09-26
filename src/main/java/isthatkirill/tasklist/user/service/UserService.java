package isthatkirill.tasklist.user.service;

import isthatkirill.tasklist.user.dto.UserDto;
import isthatkirill.tasklist.user.model.User;

/**
 * @author Kirill Emelyanov
 */

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, Long userId);

    User getByUsername(String username);

    User getById(Long id);

}
