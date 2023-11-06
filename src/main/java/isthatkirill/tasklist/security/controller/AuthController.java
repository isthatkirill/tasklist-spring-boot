package isthatkirill.tasklist.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import isthatkirill.tasklist.security.dto.JwtRequest;
import isthatkirill.tasklist.security.dto.JwtResponse;
import isthatkirill.tasklist.security.service.AuthService;
import isthatkirill.tasklist.user.dto.UserDto;
import isthatkirill.tasklist.user.service.UserService;
import isthatkirill.tasklist.validation.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kirill Emelyanov
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "AuthController", description = "Endpoints for login and registration")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public JwtResponse login(@Valid @RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{'refreshToken': 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpc3RoYXRraXJpbGwi...'}")))
    public JwtResponse refresh(@RequestBody @NotBlank String refreshToken) {
        return authService.refresh(refreshToken);
    }

}
