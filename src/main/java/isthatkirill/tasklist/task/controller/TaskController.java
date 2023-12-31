package isthatkirill.tasklist.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isthatkirill.tasklist.task.dto.TaskDtoRequest;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import isthatkirill.tasklist.task.model.enums.Priority;
import isthatkirill.tasklist.task.model.enums.Status;
import isthatkirill.tasklist.task.service.TaskService;
import isthatkirill.tasklist.util.Constants;
import isthatkirill.tasklist.validation.OnCreate;
import isthatkirill.tasklist.validation.OnUpdate;
import isthatkirill.tasklist.validation.annotation.ValidEnum;
import jakarta.validation.constraints.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/tasks")
@Tag(name = "TaskController", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@userSecurityExpression.isCorrectUserId(#userId)")
    @Operation(summary = "Create task")
    public TaskDtoResponse create(@Validated(OnCreate.class) @RequestBody TaskDtoRequest taskDto,
                                  @PathVariable Long userId) {
        return taskService.create(taskDto, userId);
    }

    @PatchMapping("/{taskId}")
    @PreAuthorize("@userSecurityExpression.isTaskOwner(#taskId, #userId)")
    @Operation(summary = "Update task")
    public TaskDtoResponse update(@Validated(OnUpdate.class) @RequestBody TaskDtoRequest taskDto,
                                  @PathVariable Long userId,
                                  @PathVariable Long taskId) {
        return taskService.update(taskDto, userId, taskId);
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("@userSecurityExpression.isTaskOwner(#taskId, #userId)")
    @Operation(summary = "Get task by id")
    public TaskDtoResponse getById(@PathVariable Long userId, @PathVariable Long taskId) {
        return taskService.getById(taskId);
    }

    @GetMapping
    @PreAuthorize("@userSecurityExpression.isCorrectUserId(#userId)")
    @Operation(summary = "Get all users tasks with filters",
            description = "You can filter by the following parameters: keyword, priority, status, expiration date, notify. Pagination is also supported.")
    public List<TaskDtoResponse> getAll(@PathVariable Long userId,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) @ValidEnum(enumClass = Priority.class) String priority,
                                        @RequestParam(required = false) @ValidEnum(enumClass = Status.class) String status,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DATE_PATTERN)
                                        @Future(message = "Task cannot expire in past") LocalDateTime expiresBefore,
                                        @RequestParam(required = false) Boolean notify,
                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                        @RequestParam(required = false, defaultValue = "10") Integer size) {
        return taskService.getAll(userId, keyword, priority, status, expiresBefore, notify, from, size);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userSecurityExpression.isTaskOwner(#taskId, #userId)")
    @Operation(summary = "Delete task by id")
    public void delete(@PathVariable Long userId, @PathVariable Long taskId) {
        taskService.delete(taskId);
    }

}
