package isthatkirill.tasklist.user.controller;

import isthatkirill.tasklist.user.dto.UserDto;
import isthatkirill.tasklist.user.service.UserService;
import isthatkirill.tasklist.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/{userId}")
    @PreAuthorize("@userSecurityExpression.canAccessUser(#userId)")
    public UserDto update(@PathVariable Long userId, @Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        return userService.update(userDto, userId);
    }

    @GetMapping
    @PreAuthorize("@userSecurityExpression.canAccessAdminEndpoints()")
    public List<UserDto> getAll() {
        return userService.getAll();
    }


}
