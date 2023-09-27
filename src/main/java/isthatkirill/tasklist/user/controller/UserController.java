package isthatkirill.tasklist.user.controller;

import isthatkirill.tasklist.user.dto.UserDto;
import isthatkirill.tasklist.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kirill Emelyanov
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @PreAuthorize("@userSecurityExpression.canAccessUser(#userId)")
    public UserDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }


}
