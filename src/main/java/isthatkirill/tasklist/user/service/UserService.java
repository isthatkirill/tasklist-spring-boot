package isthatkirill.tasklist.user.service;

import isthatkirill.tasklist.user.dto.UserDto;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, Long userId);

    UserDto getById(Long id);

    List<UserDto> getAll();

}
