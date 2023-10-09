package isthatkirill.tasklist.group.dto;

import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDtoResponse {

    Long id;
    String title;
    String description;
    String progress;
    List<TaskDtoResponse> tasks;

}
