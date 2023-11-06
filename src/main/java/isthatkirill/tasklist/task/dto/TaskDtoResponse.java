package isthatkirill.tasklist.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import isthatkirill.tasklist.util.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * @author Kirill Emelyanov
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDtoResponse {

    @Schema(description = "Id of task", example = "1")
    Long id;

    @Schema(description = "Title of the task", example = "Buy a car")
    String title;

    @Schema(description = "Title of the task", example = "Buy a ferrari")
    String description;

    @Schema(description = "Task priority", example = "LOW")
    String priority;

    @Schema(description = "Task status", example = "DONE")
    String status;

    @Schema(description = "Creation date", example = "2023-11-12 10:15:30", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime createdAt;

    @Schema(description = "Last modified date", example = "2023-11-12 16:45:45", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime lastModifiedAt;

    @Schema(description = "Expiration date", example = "2025-02-15 13:12:45", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime expiresAt;

    @Schema(description = "Is time expiration notification enabled", example = "true")
    Boolean notify;

}
