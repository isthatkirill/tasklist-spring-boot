package isthatkirill.tasklist.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/users")
@Tag(name = "UserController", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @PreAuthorize("@userSecurityExpression.canAccessUser(#userId)")
    @Operation(summary = "Get user by id")
    public UserDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("@userSecurityExpression.canAccessUser(#userId)")
    @Operation(summary = "Update user")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto,
                          @PathVariable Long userId) {
        return userService.update(userDto, userId);
    }

    @GetMapping
    @PreAuthorize("@userSecurityExpression.canAccessAdminEndpoints()")
    @Operation(summary = "Get all users", description = "Available if you have admin role. Returns a list of all users.")
    public List<UserDto> getAll() {
        return userService.getAll();
    }


}
