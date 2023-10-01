package isthatkirill.tasklist.task.mapper;

import isthatkirill.tasklist.task.dto.TaskDtoRequest;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    Task toTask(TaskDtoRequest taskDtoRequest, User owner);

    TaskDtoResponse toTaskDtoResponse(Task task);

    List<TaskDtoResponse> toTaskDtoResponse(List<Task> tasks);

}
