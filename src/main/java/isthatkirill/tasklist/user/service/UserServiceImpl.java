package isthatkirill.tasklist.user.service;

import isthatkirill.tasklist.error.exception.EntityNotFoundException;
import isthatkirill.tasklist.error.exception.NotUniqueException;
import isthatkirill.tasklist.user.dto.UserDto;
import isthatkirill.tasklist.user.mapper.UserMapper;
import isthatkirill.tasklist.user.model.Role;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        checkForUnique(userDto);
        User user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        user = userRepository.save(user);
        log.info("New user added --> {}", userDto);
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long userId) {
        User user = checkIfUserExistsAndGet(userId);
        checkForUnique(userDto);
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null) user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userRepository.save(user);
        log.info("User with id={} updated --> {}", userId, userDto);
        return userMapper.toUserDto(user);
    }


    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        log.info("Get user by username={}", username);
        return checkIfUserExistsAndGet(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        log.info("Get user by id={}", id);
        return checkIfUserExistsAndGet(id);
    }

    private User checkIfUserExistsAndGet(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    private User checkIfUserExistsAndGet(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, username));
    }

    private void checkForUnique(UserDto userDto) {
        if (userRepository.findByEmailOrUsername(userDto.getEmail(), userDto.getUsername()).isPresent()) {
            throw new NotUniqueException("Users with the same name or email already exist.");
        }
    }


}
