package isthatkirill.tasklist.security;

import isthatkirill.tasklist.model.Role;
import isthatkirill.tasklist.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kirill Emelyanov
 */

public class JwtUserFactory {

    public static JwtUser create(User user) {
        return JwtUser.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(buildAuthorities(user.getRoles()))
                .build();
    }

    private static List<GrantedAuthority> buildAuthorities(Set<Role> roles) {
        return new ArrayList<>(roles).stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
