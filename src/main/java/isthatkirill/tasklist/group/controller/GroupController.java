package isthatkirill.tasklist.group.controller;

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

    @GetMapping("/{groupId}")
    @PreAuthorize("@userSecurityExpression.isGroupOwner(#groupId, #userId)")
    public GroupDtoResponse getById(@PathVariable Long userId, @PathVariable Long groupId) {
        return groupService.getById(groupId);
    }

}
