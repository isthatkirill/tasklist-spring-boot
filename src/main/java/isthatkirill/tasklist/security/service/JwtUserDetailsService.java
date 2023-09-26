package isthatkirill.tasklist.security.service;

import isthatkirill.tasklist.error.EntityNotFoundException;
import isthatkirill.tasklist.security.model.JwtUserFactory;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = checkIfUserExistsAndGet(username);
        return JwtUserFactory.create(user);
    }

    private User checkIfUserExistsAndGet(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, username));
    }

}
