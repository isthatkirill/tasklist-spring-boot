package isthatkirill.tasklist.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    Long id;
    String title;
    String description;
    String priority;
    String status;

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime createdAt;

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime lastModifiedAt;

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime expiresAt;
    Boolean notify;

}
