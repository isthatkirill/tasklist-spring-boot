package isthatkirill.tasklist.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isthatkirill.tasklist.security.filter.JwtTokenFilter;
import isthatkirill.tasklist.task.dto.TaskDtoRequest;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import isthatkirill.tasklist.task.model.enums.Priority;
import isthatkirill.tasklist.task.model.enums.Status;
import isthatkirill.tasklist.task.service.TaskService;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static isthatkirill.tasklist.util.Constants.FORMATTER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    @MockBean
    private TaskService taskService;

    @MockBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    @MockBean
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private final TaskDtoRequest taskDtoRequest = TaskDtoRequest.builder()
            .title("title")
            .description("description")
            .priority("LOW")
            .status("NEW")
            .notify(true)
            .expiresAt(formatDate(LocalDateTime.now().plusHours(3)))
            .build();

    private final TaskDtoResponse taskDtoResponse = TaskDtoResponse.builder()
            .id(1L)
            .title("title")
            .description("description")
            .priority("LOW")
            .status("NEW")
            .notify(true)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusHours(2))
            .build();

    @Test
    @SneakyThrows
    void createTest() {
        Long userId = 1L;

        when(taskService.create(any(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(post("/api/users/{userId}/tasks", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(taskDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDtoResponse.getDescription()))
                .andExpect(jsonPath("$.priority").value(taskDtoResponse.getPriority()))
                .andExpect(jsonPath("$.status").value(taskDtoResponse.getStatus()))
                .andExpect(jsonPath("$.notify").value(taskDtoResponse.getNotify()))
                .andExpect(jsonPath("$.createdAt").value(taskDtoResponse.getCreatedAt().format(FORMATTER)))
                .andExpect(jsonPath("$.expiresAt").value(taskDtoResponse.getExpiresAt().format(FORMATTER)));

        verify(taskService).create(taskDtoRequest, userId);
    }

    @Test
    @SneakyThrows
    void createWithInvalidTitleTest() {
        taskDtoRequest.setTitle("t");
        Long userId = 1L;

        when(taskService.create(any(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(post("/api/users/{userId}/tasks", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDtoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void createWithInvalidDescriptionTest() {
        taskDtoRequest.setDescription("bad");
        Long userId = 1L;

        when(taskService.create(any(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(post("/api/users/{userId}/tasks", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDtoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void createWithInvalidPriorityTest() {
        taskDtoRequest.setPriority("MAXIMUM");
        Long userId = 1L;

        when(taskService.create(any(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(post("/api/users/{userId}/tasks", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDtoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void createWithInvalidStatusTest() {
        taskDtoRequest.setStatus("NO_STATUS");
        Long userId = 1L;

        when(taskService.create(any(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(post("/api/users/{userId}/tasks", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDtoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void createWithInvalidExpiresAtTest() {
        taskDtoRequest.setExpiresAt(LocalDateTime.now().minusHours(1));
        Long userId = 1L;

        when(taskService.create(any(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(post("/api/users/{userId}/tasks", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDtoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void updateTest() {
        Long userId = 1L;
        Long taskId = 1L;

        when(taskService.update(any(), anyLong(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(patch("/api/users/{userId}/tasks/{taskId}", userId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(taskDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDtoResponse.getDescription()))
                .andExpect(jsonPath("$.priority").value(taskDtoResponse.getPriority()))
                .andExpect(jsonPath("$.status").value(taskDtoResponse.getStatus()))
                .andExpect(jsonPath("$.notify").value(taskDtoResponse.getNotify()))
                .andExpect(jsonPath("$.createdAt").value(taskDtoResponse.getCreatedAt().format(FORMATTER)))
                .andExpect(jsonPath("$.expiresAt").value(taskDtoResponse.getExpiresAt().format(FORMATTER)));

        verify(taskService).update(taskDtoRequest, userId, taskId);
    }

    @Test
    @SneakyThrows
    void updateWithAllNullFieldsTest() {
        TaskDtoRequest update = new TaskDtoRequest();
        Long userId = 1L;
        Long taskId = 1L;

        when(taskService.update(any(), anyLong(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(patch("/api/users/{userId}/tasks/{taskId}", userId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(taskDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDtoResponse.getDescription()))
                .andExpect(jsonPath("$.priority").value(taskDtoResponse.getPriority()))
                .andExpect(jsonPath("$.status").value(taskDtoResponse.getStatus()))
                .andExpect(jsonPath("$.notify").value(taskDtoResponse.getNotify()))
                .andExpect(jsonPath("$.createdAt").value(taskDtoResponse.getCreatedAt().format(FORMATTER)))
                .andExpect(jsonPath("$.expiresAt").value(taskDtoResponse.getExpiresAt().format(FORMATTER)));

        verify(taskService).update(update, userId, taskId);
    }

    @Test
    @SneakyThrows
    void updateWithInvalidExpiresAtTest() {
        TaskDtoRequest update = TaskDtoRequest.builder().expiresAt(LocalDateTime.now().minusHours(1)).build();
        Long userId = 1L;
        Long taskId = 1L;

        when(taskService.update(any(), anyLong(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(patch("/api/users/{userId}/tasks/{taskId}", userId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void updateWithInvalidEnumTest() {
        TaskDtoRequest update = TaskDtoRequest.builder().status("STATUS").build();
        Long userId = 1L;
        Long taskId = 1L;

        when(taskService.update(any(), anyLong(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(patch("/api/users/{userId}/tasks/{taskId}", userId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void updateWithInvalidTitleTest() {
        TaskDtoRequest update = TaskDtoRequest.builder().title("p").build();
        Long userId = 1L;
        Long taskId = 1L;

        when(taskService.update(any(), anyLong(), anyLong())).thenReturn(taskDtoResponse);

        mvc.perform(patch("/api/users/{userId}/tasks/{taskId}", userId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void getByIdTest() {
        Long userId = 1L;
        Long taskId = 1L;

        when(taskService.getById(any())).thenReturn(taskDtoResponse);

        mvc.perform(get("/api/users/{userId}/tasks/{taskId}", userId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskDtoResponse.getId()))
                .andExpect(jsonPath("$.title").value(taskDtoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDtoResponse.getDescription()))
                .andExpect(jsonPath("$.priority").value(taskDtoResponse.getPriority()))
                .andExpect(jsonPath("$.status").value(taskDtoResponse.getStatus()))
                .andExpect(jsonPath("$.notify").value(taskDtoResponse.getNotify()))
                .andExpect(jsonPath("$.createdAt").value(taskDtoResponse.getCreatedAt().format(FORMATTER)))
                .andExpect(jsonPath("$.expiresAt").value(taskDtoResponse.getExpiresAt().format(FORMATTER)));

        verify(taskService).getById(taskId);
    }

    @Test
    @SneakyThrows
    void getAllWithParamsTest() {
        Long userId = 1L;
        String keyword = "keyword";
        String priority = Priority.LOW.name();
        String status = Status.NEW.name();
        LocalDateTime expiresBefore = LocalDateTime.now().plusHours(3);
        Boolean notify = true;
        Integer from = 1;
        Integer size = 2;

        when(taskService.getAll(anyLong(), anyString(), anyString(), anyString(),
                any(), anyBoolean(), anyInt(), anyInt())).thenReturn(List.of(taskDtoResponse));

        mvc.perform(get("/api/users/{userId}/tasks", userId)
                        .param("keyword", keyword)
                        .param("priority", priority)
                        .param("status", status)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .param("notify", String.valueOf(notify))
                        .param("expiresBefore", String.valueOf(expiresBefore))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(taskDtoResponse.getId()))
                .andExpect(jsonPath("$[0].title").value(taskDtoResponse.getTitle()))
                .andExpect(jsonPath("$[0].description").value(taskDtoResponse.getDescription()))
                .andExpect(jsonPath("$[0].priority").value(taskDtoResponse.getPriority()))
                .andExpect(jsonPath("$[0].status").value(taskDtoResponse.getStatus()))
                .andExpect(jsonPath("$[0].notify").value(taskDtoResponse.getNotify()))
                .andExpect(jsonPath("$[0].createdAt").value(taskDtoResponse.getCreatedAt().format(FORMATTER)))
                .andExpect(jsonPath("$[0].expiresAt").value(taskDtoResponse.getExpiresAt().format(FORMATTER)));

        verify(taskService).getAll(userId, keyword, priority, status, expiresBefore, notify, from, size);
    }

    @Test
    @SneakyThrows
    void getAllWithoutParamsTest() {
        Long userId = 1L;
        Integer defaultFrom = 0;
        Integer defaultSize = 10;

        when(taskService.getAll(anyLong(), any(), any(), any(),
                any(), any(), anyInt(), anyInt())).thenReturn(List.of(taskDtoResponse));

        mvc.perform(get("/api/users/{userId}/tasks", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(taskDtoResponse.getId()))
                .andExpect(jsonPath("$[0].title").value(taskDtoResponse.getTitle()))
                .andExpect(jsonPath("$[0].description").value(taskDtoResponse.getDescription()))
                .andExpect(jsonPath("$[0].priority").value(taskDtoResponse.getPriority()))
                .andExpect(jsonPath("$[0].status").value(taskDtoResponse.getStatus()))
                .andExpect(jsonPath("$[0].notify").value(taskDtoResponse.getNotify()))
                .andExpect(jsonPath("$[0].createdAt").value(taskDtoResponse.getCreatedAt().format(FORMATTER)))
                .andExpect(jsonPath("$[0].expiresAt").value(taskDtoResponse.getExpiresAt().format(FORMATTER)));

        verify(taskService).getAll(userId, null, null, null, null, null, defaultFrom, defaultSize);
    }

    @Test
    @SneakyThrows
    void getAllWithInvalidEnumTest() {
        Long userId = 1L;
        String status = "BAD";

        when(taskService.getAll(anyLong(), any(), any(), any(),
                any(), any(), anyInt(), anyInt())).thenReturn(List.of(taskDtoResponse));

        mvc.perform(get("/api/users/{userId}/tasks", userId)
                        .param("status", status)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void getAllWithInvalidExpiresBeforeTest() {
        Long userId = 1L;
        LocalDateTime expiresBefore = LocalDateTime.now().minusHours(2);

        when(taskService.getAll(anyLong(), any(), any(), any(),
                any(), any(), anyInt(), anyInt())).thenReturn(List.of(taskDtoResponse));

        mvc.perform(get("/api/users/{userId}/tasks", userId)
                        .param("expiresBefore", String.valueOf(expiresBefore))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verifyNoInteractions(taskService);
    }

    @Test
    @SneakyThrows
    void deleteTest() {
        Long userId = 1L;
        Long taskId = 1L;

        mvc.perform(delete("/api/users/{userId}/tasks/{taskId}", userId, taskId)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent());

        verify(taskService).delete(taskId);
    }

    private LocalDateTime formatDate(LocalDateTime localDateTime) {
        return LocalDateTime.parse(localDateTime.format(FORMATTER), FORMATTER);
    }

}