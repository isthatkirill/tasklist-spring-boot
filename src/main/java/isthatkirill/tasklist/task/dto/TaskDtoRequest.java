package isthatkirill.tasklist.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import isthatkirill.tasklist.task.model.enums.Priority;
import isthatkirill.tasklist.task.model.enums.Status;
import isthatkirill.tasklist.util.Constants;
import isthatkirill.tasklist.validation.OnCreate;
import isthatkirill.tasklist.validation.OnUpdate;
import isthatkirill.tasklist.validation.annotation.ValidEnum;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class TaskDtoRequest {

    @Schema(description = "Title of the task", example = "Buy a car", minLength = 2, maxLength = 128)
    @NotBlank(message = "Title cannot be blank", groups = OnCreate.class)
    @Size(
            min = 2,
            max = 128,
            message = "Title must be from 2 to 128 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    String title;

    @Schema(description = "Description of the task", example = "Buy a ferrari", minLength = 8, maxLength = 512)
    @NotBlank(message = "Description cannot be blank", groups = OnCreate.class)
    @Size(
            min = 8,
            max = 512,
            message = "Description must be from 8 to 512 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    String description;

    @Schema(description = "Task priority", example = "LOW", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @ValidEnum(enumClass = Priority.class,
            groups = {OnCreate.class, OnUpdate.class})
    String priority = Priority.DEFAULT.name();

    @Schema(description = "Task status", example = "NEW", allowableValues = {"NEW", "IN_PROGRESS", "DONE"})
    @ValidEnum(enumClass = Status.class,
            groups = {OnCreate.class, OnUpdate.class})
    String status = Status.NEW.name();

    @Schema(description = "Expiration date", example = "2025-12-12 10:15:30", format = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "Task cannot expire in past", groups = {OnCreate.class, OnUpdate.class})
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime expiresAt;

    @Schema(description = "Is time expiration notification enabled", example = "true")
    Boolean notify = true;

}
