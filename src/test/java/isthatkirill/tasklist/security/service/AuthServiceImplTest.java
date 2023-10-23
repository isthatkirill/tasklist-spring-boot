package isthatkirill.tasklist.security.service;

import isthatkirill.tasklist.security.dto.JwtRequest;
import isthatkirill.tasklist.security.dto.JwtResponse;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kirill Emelyanov
 */

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void loginTest() {
        String password = "password";
        String username = "username";
        String accessToken = "access";
        String refreshToken = "refresh";

        User user = User.builder()
                .id(1L)
                .email("email@email.com")
                .name("name")
                .username(username)
                .password(password)
                .roles(Collections.emptySet())
                .build();

        JwtRequest jwtRequest = JwtRequest.builder()
                .password(username)
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createAccessToken(user.getId(), username, user.getRoles())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(user.getId(), username)).thenReturn(refreshToken);

        JwtResponse response = authService.login(jwtRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                        jwtRequest.getPassword()
                )
        );
        assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("accessToken", accessToken)
                .hasFieldOrPropertyWithValue("refreshToken", refreshToken);
        assertThat(username).isEqualTo(response.getUsername());
        assertThat(user.getId()).isEqualTo(response.getId());

    }

    @Test
    void refresh() {
    }

}