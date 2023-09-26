package isthatkirill.tasklist.user.service;

import isthatkirill.tasklist.user.dto.UserDto;
import isthatkirill.tasklist.user.mapper.UserMapper;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        //TODO chekings for email and username
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        log.info("Get user by username={}", username);

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        log.info("Get user by id={}", id);


        return null;
    }


}
