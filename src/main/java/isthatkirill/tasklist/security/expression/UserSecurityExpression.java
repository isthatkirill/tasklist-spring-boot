package isthatkirill.tasklist.security.expression;

import isthatkirill.tasklist.security.model.JwtUser;
import isthatkirill.tasklist.user.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Kirill Emelyanov
 */

@Service("userSecurityExpression")
public class UserSecurityExpression {

    public boolean canAccessUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser user = (JwtUser) authentication.getPrincipal();
        Long id = user.getId();
        return userId.equals(id) || isAdmin(authentication);
    }

    public boolean canAccessAdminEndpoints() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isAdmin(authentication);
    }

    private boolean isAdmin(Authentication authentication) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Role.ROLE_ADMIN.name());
        return authentication.getAuthorities().contains(authority);
    }

}
