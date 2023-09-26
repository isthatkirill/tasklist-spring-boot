package isthatkirill.tasklist.user.mapper;

import isthatkirill.tasklist.user.dto.UserDto;
import isthatkirill.tasklist.user.model.User;
import org.mapstruct.Mapper;

/**
 * @author Kirill Emelyanov
 */

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

}
