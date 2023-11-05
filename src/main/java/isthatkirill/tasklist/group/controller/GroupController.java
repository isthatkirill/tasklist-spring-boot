package isthatkirill.tasklist.group.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import isthatkirill.tasklist.group.dto.GroupDtoRequest;
import isthatkirill.tasklist.group.dto.GroupDtoResponse;
import isthatkirill.tasklist.group.service.GroupService;
import isthatkirill.tasklist.validation.OnCreate;
import isthatkirill.tasklist.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kirill Emelyanov
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/groups")
@Tag(name = "GroupController", description = "Endpoints for managing groups")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@userSecurityExpression.isCorrectUserId(#userId)")
    public GroupDtoResponse create(@Validated(OnCreate.class) @RequestBody GroupDtoRequest groupDtoRequest,
                                   @PathVariable Long userId) {
        return groupService.create(groupDtoRequest, userId);
    }

    @PatchMapping("/{groupId}")
    @PreAuthorize("@userSecurityExpression.isGroupOwner(#groupId, #userId)")
    public GroupDtoResponse update(@Validated(OnUpdate.class) @RequestBody GroupDtoRequest groupDtoRequest,
                                   @PathVariable Long userId,
                                   @PathVariable Long groupId) {
        return groupService.update(groupDtoRequest, groupId, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{groupId}")
    @PreAuthorize("@userSecurityExpression.isGroupOwner(#groupId, #userId)")
    public void delete(@PathVariable Long userId, @PathVariable Long groupId) {
        groupService.delete(groupId);
    }

    @PatchMapping("/{groupId}/join/{taskId}")
    @PreAuthorize("@userSecurityExpression.isGroupOwner(#groupId, #userId) && @userSecurityExpression.isTaskOwner(#taskId, #userId)")
    public GroupDtoResponse addTaskInGroup(@PathVariable Long userId,
                                           @PathVariable Long groupId,
                                           @PathVariable Long taskId) {
        return groupService.addTaskInGroup(userId, groupId, taskId);
    }

    @DeleteMapping("/{groupId}/delete/{taskId}")
    @PreAuthorize("@userSecurityExpression.isGroupOwner(#groupId, #userId) && @userSecurityExpression.isTaskOwner(#taskId, #userId)")
    public GroupDtoResponse deleteTaskFromGroup(@PathVariable Long userId,
                                                @PathVariable Long groupId,
                                                @PathVariable Long taskId) {
        return groupService.deleteTaskFromGroup(userId, groupId, taskId);
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("@userSecurityExpression.isGroupOwner(#groupId, #userId)")
    public GroupDtoResponse getById(@PathVariable Long userId, @PathVariable Long groupId) {
        return groupService.getById(groupId);
    }

}
