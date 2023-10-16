package isthatkirill.tasklist.group.service;

import isthatkirill.tasklist.group.dto.GroupDtoRequest;
import isthatkirill.tasklist.group.dto.GroupDtoResponse;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Kirill Emelyanov
 */

public interface GroupService {

    GroupDtoResponse create(GroupDtoRequest groupDtoRequest, Long userId);

    GroupDtoResponse update(GroupDtoRequest groupDtoRequest, Long groupId, Long userId);

    GroupDtoResponse addTaskInGroup(Long userId, Long groupId, Long taskId);

    GroupDtoResponse deleteTaskFromGroup(Long userId, Long groupId, Long taskId);

    GroupDtoResponse getById(Long groupId);

}
