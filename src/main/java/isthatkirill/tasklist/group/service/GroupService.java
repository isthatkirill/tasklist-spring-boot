package isthatkirill.tasklist.group.service;

import isthatkirill.tasklist.group.dto.GroupDtoRequest;
import isthatkirill.tasklist.group.dto.GroupDtoResponse;

/**
 * @author Kirill Emelyanov
 */

public interface GroupService {

    GroupDtoResponse create(GroupDtoRequest groupDtoRequest, Long userId);

    GroupDtoResponse update(GroupDtoRequest groupDtoRequest, Long groupId, Long userId);

    GroupDtoResponse addTaskInGroup(Long userId, Long groupId, Long taskId);

    GroupDtoResponse deleteTaskFromGroup(Long userId, Long groupId, Long taskId);

    void delete(Long groupId);

    GroupDtoResponse getById(Long groupId);

}
