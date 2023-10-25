package isthatkirill.tasklist.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.user.dto.UserDto;
import isthatkirill.tasklist.user.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto = UserDto.builder()
            .name("kirill")
            .email("kirill@yandex.ru")
            .username("isthatkirill")
            .password("password")
            .build();

    private final Long userId = 1L;

    @Test
    @SneakyThrows
    void getByIdTest() {
        when(userService.getById(any())).thenReturn(userDto);

        mvc.perform(get("/api/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()))
                .andExpect(jsonPath("$.password").hasJsonPath());

        verify(userService).getById(userId);
    }

    @Test
    @SneakyThrows
    void getByIdNonExistentTest() {
        when(userService.getById(any())).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/api/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userService).getById(userId);
    }

    @Test
    @SneakyThrows
    void updateTest() {
        when(userService.update(any(), anyLong())).thenReturn(userDto);

        mvc.perform(patch("/api/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()))
                .andExpect(jsonPath("$.password").hasJsonPath());

        verify(userService).update(userDto, userId);
    }

    @Test
    @SneakyThrows
    void updateWithInvalidNameTest() {
        userDto.setName("n");

        when(userService.update(any(), anyLong())).thenReturn(userDto);

        mvc.perform(patch("/api/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void updateWithInvalidEmailTest() {
        userDto.setEmail("not.email");

        when(userService.update(any(), anyLong())).thenReturn(userDto);

        mvc.perform(patch("/api/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        verifyNoInteractions(userService);
    }

    @Test
    @SneakyThrows
    void getAllTest() {
        when(userService.getAll()).thenReturn(List.of(userDto));

        mvc.perform(get("/api/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()))
                .andExpect(jsonPath("$[0].username").value(userDto.getUsername()))
                .andExpect(jsonPath("$[0].password").hasJsonPath());

        verify(userService).getAll();
    }

}