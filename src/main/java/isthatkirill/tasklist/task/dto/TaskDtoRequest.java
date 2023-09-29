package isthatkirill.tasklist.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    Long id;

    @NotBlank(message = "Title cannot be blank", groups = OnCreate.class)
    @Size(
            min = 2,
            max = 128,
            message = "Title must be from 2 to 128 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    String title;

    @NotBlank(message = "Description cannot be blank", groups = OnCreate.class)
    @Size(
            min = 8,
            max = 512,
            message = "Description must be from 8 to 512 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    String description;

    @ValidEnum(enumClass = Priority.class,
            groups = {OnCreate.class, OnUpdate.class})
    String priority = Priority.DEFAULT.name();

    @ValidEnum(enumClass = Status.class,
            groups = {OnCreate.class, OnUpdate.class})
    String status = Status.NEW.name();

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime lastModifiedAt;

    @Future(message = "Task cannot expire in past")
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    LocalDateTime expiresAt;
    Boolean notify = true;

}
