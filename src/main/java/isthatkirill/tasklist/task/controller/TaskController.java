package isthatkirill.tasklist.task.controller;

import isthatkirill.tasklist.task.dto.TaskDtoRequest;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import isthatkirill.tasklist.task.service.TaskService;
import isthatkirill.tasklist.validation.OnCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kirill Emelyanov
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("@userSecurityExpression.isCorrectUserId(#userId)")
    public TaskDtoResponse create(@Validated(OnCreate.class) @RequestBody TaskDtoRequest taskDto,
                                  @PathVariable Long userId) {
        return taskService.create(taskDto, userId);
    }

}
