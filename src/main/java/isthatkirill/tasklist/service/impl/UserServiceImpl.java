package isthatkirill.tasklist.service.impl;

import isthatkirill.tasklist.model.User;
import isthatkirill.tasklist.repositorty.UserRepository;
import isthatkirill.tasklist.service.UserService;
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
