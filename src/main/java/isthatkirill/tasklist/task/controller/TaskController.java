package isthatkirill.tasklist.task.controller;

import isthatkirill.tasklist.task.dto.UserTaskDto;
import isthatkirill.tasklist.task.service.TaskService;
import isthatkirill.tasklist.validation.OnCreate;
import lombok.RequiredArgsConstructor;
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
    public UserTaskDto create(@Validated(OnCreate.class) @RequestBody UserTaskDto taskDto,
                              @PathVariable Long userId) {
        return taskService.create(taskDto, userId);
    }

}
