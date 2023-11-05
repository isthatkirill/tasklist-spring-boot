package isthatkirill.tasklist.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Id of group", example = "1")
    Long id;

    @Schema(description = "Title of group", example = "Preparing for a birthday")
    String title;

    @Schema(description = "Description of group", example = "Prepare for mother's birthday")
    String description;

    @Schema(description = "Number of completed tasks", example = "1/2")
    String progress;

    @Schema(description = "List of tasks")
    List<TaskDtoResponse> tasks;

}
