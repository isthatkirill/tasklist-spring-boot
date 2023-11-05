package isthatkirill.tasklist.security.filter;

import isthatkirill.tasklist.security.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

    @Autowired
    private JwtTokenFilter filter;

    @MockBean
    private JwtTokenProvider provider;

    @Test
    @SneakyThrows
    void doFilterCorrectTest() {
        String token = "CORRECTLY_STARTED";
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        Authentication authentication = mock(Authentication.class);

        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(provider.isValidToken(any())).thenReturn(true);
        when(provider.getAuthentication(any())).thenReturn(authentication);

        assertDoesNotThrow(() -> filter.doFilter(servletRequest, servletResponse, filterChain));

        verify(servletRequest).getHeader("Authorization");
        verify(provider).isValidToken(token);
        verify(provider).getAuthentication(token);
    }

    @Test
    @SneakyThrows
    void doFilterWithInvalidTokenTest() {
        String token = "INCORRECT";
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        Authentication authentication = mock(Authentication.class);

        when(servletRequest.getHeader("Authorization")).thenReturn(token);
        when(provider.isValidToken(any())).thenReturn(false);
        when(provider.getAuthentication(any())).thenReturn(authentication);

        assertDoesNotThrow(() -> filter.doFilter(servletRequest, servletResponse, filterChain));

        verify(servletRequest).getHeader("Authorization");
        verify(provider).isValidToken(token);
        verifyNoMoreInteractions(provider);
    }

}