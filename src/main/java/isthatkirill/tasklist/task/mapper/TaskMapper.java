package isthatkirill.tasklist.task.mapper;

import isthatkirill.tasklist.task.dto.UserTaskDto;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kirill Emelyanov
 */

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "id", ignore = true)
    Task toTask(UserTaskDto userTaskDto, User owner);

    UserTaskDto toUserTaskDto(Task task);

}
