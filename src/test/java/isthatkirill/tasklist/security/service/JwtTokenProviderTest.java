package isthatkirill.tasklist.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import isthatkirill.tasklist.security.dto.JwtResponse;
import isthatkirill.tasklist.security.model.JwtUser;
import isthatkirill.tasklist.security.service.props.JwtProperties;
import isthatkirill.tasklist.user.model.Role;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider provider;

    @Autowired
    private JwtProperties jwtProperties;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    private Key key;

    @BeforeEach
    public void setupKey() {
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Test
    void createAccessTokenTest() {
        Long userId = 1L;
        String username = "username";
        Set<Role> roles = Set.of(Role.ROLE_USER);
        List<String> rolesAsString = roles.stream()
                .map(Enum::name)
                .toList();

        String token = provider.createAccessToken(userId, username, roles);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Claims body = claims.getBody();
        Long timeDiff = body.getExpiration().getTime() - body.getIssuedAt().getTime();

        assertThat(body.getSubject()).isEqualTo("username");
        assertThat(body.get("id", Long.class)).isEqualTo(userId);
        assertThat(body.get("roles", List.class)).isEqualTo(rolesAsString);
        assertThat(timeDiff).isEqualTo(jwtProperties.getAccess());
    }

    @Test
    void createRefreshTokenTest() {
        Long userId = 1L;
        String username = "username";

        String token = provider.createRefreshToken(userId, username);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Claims body = claims.getBody();
        Long timeDiff = body.getExpiration().getTime() - body.getIssuedAt().getTime();

        assertThat(body.getSubject()).isEqualTo("username");
        assertThat(body.get("id", Long.class)).isEqualTo(userId);
        assertThat(timeDiff).isEqualTo(jwtProperties.getRefresh());
    }

    @Test
    @SneakyThrows
    void refreshUserTokensTest() {
        User user = User.builder()
                .id(1L)
                .username("username")
                .name("name")
                .password("password")
                .email("email@email.ru")
                .roles(Set.of(Role.ROLE_USER))
                .build();
        Optional<User> optionalUser = Optional.of(user);
        String refreshOld = provider.createRefreshToken(user.getId(), user.getUsername());
        String accessOld = provider.createAccessToken(user.getId(), user.getUsername(), user.getRoles());

        when(userRepository.findById(user.getId())).thenReturn(optionalUser);

        Executable waitExecutable = () -> TimeUnit.SECONDS.sleep(1); //need to wait because of Date precision
        waitExecutable.execute();                                           // otherwise there is a chance to get old tokens

        JwtResponse response = provider.refreshUserTokens(refreshOld);

        assertThat(response).isNotNull()
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("username", user.getUsername());

        assertThat(refreshOld).isNotEqualTo(response.getRefreshToken());
        assertThat(accessOld).isNotEqualTo(response.getAccessToken());
        assertThat(extractExpiration(accessOld)).isLessThan(extractExpiration(response.getAccessToken()));
        assertThat(extractExpiration(refreshOld)).isLessThan(extractExpiration(response.getRefreshToken()));

        verify(userRepository).findById(user.getId());
    }

    @Test
    void refreshUserTokensWithExpiredTest() {
        Claims claims = Jwts.claims().setSubject("username");
        Date now = new Date();
        Date validity = new Date(now.getTime() - 1000);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> provider.refreshUserTokens(token));
    }

    @Test
    void getAuthentication() {
        Claims claims = Jwts.claims().setSubject("username");
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();

        JwtUser jwtUser = JwtUser.builder()
                .id(1L)
                .password("password")
                .name("name")
                .email("email")
                .username("username")
                .authorities(List.of(new SimpleGrantedAuthority("USER_ROLE"))).build();

        when(userDetailsService.loadUserByUsername(any())).thenReturn(jwtUser);

        Authentication authentication = provider.getAuthentication(token);
        JwtUser principal = (JwtUser) authentication.getPrincipal();

        assertThat(principal).isNotNull()
                .isEqualTo(jwtUser);
    }

    @Test
    void isValidTokenBadCaseTest() {
        Claims claims = Jwts.claims().setSubject("username");
        Date now = new Date();
        Date validity = new Date(now.getTime() - 1000);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> provider.isValidToken(token));
    }

    @Test
    void isValidTokenGoodCaseTest() {
        Claims claims = Jwts.claims().setSubject("username");
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();

        boolean isValid = provider.isValidToken(token);
        assertThat(isValid).isTrue();
    }

    private Long extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime();
    }


}