package isthatkirill.tasklist.task.dto;

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
public class AdminTaskDto {

    Long id;
    String title;
    String description;
    String priority;
    LocalDateTime createdAt;
    LocalDateTime lastModifiedAt;
    LocalDateTime expiresAt;
    Boolean notify;
    Long ownerId;

}
