package isthatkirill.tasklist.group.mapper;

import isthatkirill.tasklist.group.dto.GroupDtoRequest;
import isthatkirill.tasklist.group.dto.GroupDtoResponse;
import isthatkirill.tasklist.group.model.Group;
import isthatkirill.tasklist.task.mapper.TaskMapper;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    Group toGroup(GroupDtoRequest groupDtoRequest, List<Task> tasks, User owner);

    @Mapping(target = "progress", ignore = true)
    GroupDtoResponse toGroupDtoResponse(Group group);

}
