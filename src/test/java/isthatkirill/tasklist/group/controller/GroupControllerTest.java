package isthatkirill.tasklist.group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.group.dto.GroupDtoRequest;
import isthatkirill.tasklist.group.dto.GroupDtoResponse;
import isthatkirill.tasklist.group.service.GroupService;
import isthatkirill.tasklist.security.filter.JwtTokenFilter;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = GroupController.class)
class GroupControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    @MockBean
    private JwtTokenFilter jwtTokenFilter;

    private final GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
            .title("title")
            .description("description")
            .taskIds(List.of(1L))
            .build();

    private final GroupDtoResponse groupDtoResponse = GroupDtoResponse.builder()
            .id(1L)
            .title("title")
            .description("description")
            .progress("0/1")
            .tasks(List.of(
                    TaskDtoResponse.builder().id(1L).title("task_title").build()
            ))
            .build();

    @Test
    @SneakyThrows
    void createTest() {
        Long userId = 1L;

        when(groupService.create(any(), anyLong())).thenReturn(groupDtoResponse);

        mvc.perform(post("/api/users/{userId}/groups", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(groupDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(groupDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(groupDtoResponse.getDescription()))
                .andExpect(jsonPath("$.progress").value(groupDtoResponse.getProgress()))
                .andExpect(jsonPath("$.tasks[0].id").value(groupDtoResponse.getTasks().get(0).getId()));

        verify(groupService).create(groupDtoRequest, userId);
    }

    @Test
    @SneakyThrows
    void createWithTooShortTitleTest() {
        groupDtoRequest.setTitle("t");
        Long userId = 1L;

        mvc.perform(post("/api/users/{userId}/groups", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(groupService);
    }

    @Test
    @SneakyThrows
    void createWithTooShortDescriptionTest() {
        groupDtoRequest.setDescription("desc");
        Long userId = 1L;

        mvc.perform(post("/api/users/{userId}/groups", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(groupService);
    }

    @Test
    @SneakyThrows
    void updateTest() {
        Long userId = 1L;
        Long groupId = 1L;

        when(groupService.update(any(), anyLong(), anyLong())).thenReturn(groupDtoResponse);

        mvc.perform(patch("/api/users/{userId}/groups/{groupId}", userId, groupId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(groupDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(groupDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(groupDtoResponse.getDescription()))
                .andExpect(jsonPath("$.progress").value(groupDtoResponse.getProgress()))
                .andExpect(jsonPath("$.tasks[0].id").value(groupDtoResponse.getTasks().get(0).getId()));

        verify(groupService).update(groupDtoRequest, groupId, userId);
    }

    @Test
    @SneakyThrows
    void updateWithNullFieldTest() {
        groupDtoRequest.setTitle(null);
        Long userId = 1L;
        Long groupId = 1L;

        when(groupService.update(any(), anyLong(), anyLong())).thenReturn(groupDtoResponse);

        mvc.perform(patch("/api/users/{userId}/groups/{groupId}", userId, groupId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(groupDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(groupDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(groupDtoResponse.getDescription()))
                .andExpect(jsonPath("$.progress").value(groupDtoResponse.getProgress()))
                .andExpect(jsonPath("$.tasks[0].id").value(groupDtoResponse.getTasks().get(0).getId()));

        verify(groupService).update(groupDtoRequest, groupId, userId);
    }

    @Test
    @SneakyThrows
    void updateWithTooShortTitleTest() {
        groupDtoRequest.setTitle("d");
        Long userId = 1L;
        Long groupId = 1L;

        mvc.perform(patch("/api/users/{userId}/groups/{groupId}", userId, groupId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(groupService);
    }

    @Test
    @SneakyThrows
    void updateWithTooShortDescriptionTest() {
        groupDtoRequest.setDescription("desc");
        Long userId = 1L;
        Long groupId = 1L;

        mvc.perform(patch("/api/users/{userId}/groups/{groupId}", userId, groupId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(groupService);
    }

    @Test
    @SneakyThrows
    void deleteTest() {
        Long userId = 1L;
        Long groupId = 1L;

        mvc.perform(delete("/api/users/{userId}/groups/{groupId}", userId, groupId)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent());

        verify(groupService).delete(groupId);
    }

    @Test
    @SneakyThrows
    void addTaskInGroupTest() {
        Long userId = 1L;
        Long groupId = 1L;
        Long taskId = 1L;

        when(groupService.addTaskInGroup(anyLong(), anyLong(), anyLong())).thenReturn(groupDtoResponse);

        mvc.perform(patch("/api/users/{userId}/groups/{groupId}/join/{taskId}", userId, groupId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(groupDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(groupDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(groupDtoResponse.getDescription()))
                .andExpect(jsonPath("$.progress").value(groupDtoResponse.getProgress()))
                .andExpect(jsonPath("$.tasks[0].id").value(groupDtoResponse.getTasks().get(0).getId()));

        verify(groupService).addTaskInGroup(userId, groupId, taskId);
    }

    @Test
    @SneakyThrows
    void addSameTaskInGroupTest() {
        Long userId = 1L;
        Long groupId = 1L;
        Long taskId = 1L;

        when(groupService.addTaskInGroup(anyLong(), anyLong(), anyLong())).thenThrow(IllegalStateException.class);

        mvc.perform(patch("/api/users/{userId}/groups/{groupId}/join/{taskId}", userId, groupId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(groupService).addTaskInGroup(userId, groupId, taskId);
    }

    @Test
    @SneakyThrows
    void deleteTaskFromGroup() {
        Long userId = 1L;
        Long groupId = 1L;
        Long taskId = 1L;

        when(groupService.deleteTaskFromGroup(anyLong(), anyLong(), anyLong())).thenReturn(groupDtoResponse);

        mvc.perform(delete("/api/users/{userId}/groups/{groupId}/delete/{taskId}", userId, groupId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(groupDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(groupDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(groupDtoResponse.getDescription()))
                .andExpect(jsonPath("$.progress").value(groupDtoResponse.getProgress()))
                .andExpect(jsonPath("$.tasks[0].id").value(groupDtoResponse.getTasks().get(0).getId()));

        verify(groupService).deleteTaskFromGroup(userId, groupId, taskId);
    }

    @Test
    @SneakyThrows
    void deleteNonExistentTaskFromGroup() {
        Long userId = 1L;
        Long groupId = 1L;
        Long taskId = 1L;

        when(groupService.deleteTaskFromGroup(anyLong(), anyLong(), anyLong())).thenThrow(EntityNotFoundException.class);

        mvc.perform(delete("/api/users/{userId}/groups/{groupId}/delete/{taskId}", userId, groupId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(groupService).deleteTaskFromGroup(userId, groupId, taskId);
    }

    @Test
    @SneakyThrows
    void getByIdTest() {
        Long userId = 1L;
        Long groupId = 1L;

        when(groupService.getById(anyLong())).thenReturn(groupDtoResponse);

        mvc.perform(get("/api/users/{userId}/groups/{groupId}", userId, groupId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(groupDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(groupDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(groupDtoResponse.getDescription()))
                .andExpect(jsonPath("$.progress").value(groupDtoResponse.getProgress()))
                .andExpect(jsonPath("$.tasks[0].id").value(groupDtoResponse.getTasks().get(0).getId()));

        verify(groupService).getById(groupId);
    }

    @Test
    @SneakyThrows
    void getByNonExistentIdTest() {
        Long userId = 1L;
        Long groupId = 1L;

        when(groupService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/api/users/{userId}/groups/{groupId}", userId, groupId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(groupService).getById(groupId);
    }

}