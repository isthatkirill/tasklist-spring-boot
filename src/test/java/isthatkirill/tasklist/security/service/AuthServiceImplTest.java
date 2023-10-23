package isthatkirill.tasklist.security.service;

import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.security.dto.JwtRequest;
import isthatkirill.tasklist.security.dto.JwtResponse;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
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
                .username(username)
                .password(password)
                .roles(Collections.emptySet())
                .build();

        JwtRequest jwtRequest = JwtRequest.builder()
                .password(password)
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
    void loginByNonExistentUserTest() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .password("password")
                .username("username")
                .build();

        assertThrows(EntityNotFoundException.class, () -> authService.login(jwtRequest));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                        jwtRequest.getPassword()
                )
        );

        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void refreshTest() {
        String accessToken = "access";
        String refreshToken = "refresh";
        String newRefreshToken = "newRefresh";
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();

        when(jwtTokenProvider.refreshUserTokens(refreshToken))
                .thenReturn(jwtResponse);

        JwtResponse testResponse = authService.refresh(refreshToken);

        verify(jwtTokenProvider).refreshUserTokens(refreshToken);
        assertThat(testResponse).isNotNull()
                .isEqualTo(jwtResponse);
    }

}