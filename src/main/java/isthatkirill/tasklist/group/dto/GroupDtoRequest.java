package isthatkirill.tasklist.group.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import isthatkirill.tasklist.validation.OnCreate;
import isthatkirill.tasklist.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDtoRequest {

    @Schema(description = "Title of the group", example = "Preparing for a birthday", minLength = 2, maxLength = 128)
    @NotBlank(message = "Title cannot be blank", groups = OnCreate.class)
    @Size(
            min = 2,
            max = 128,
            message = "Title must be from 2 to 128 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    String title;

    @Schema(description = "Description of the group", example = "Prepare for mother's birthday", minLength = 8, maxLength = 512)
    @NotBlank(message = "Description cannot be blank", groups = OnCreate.class)
    @Size(
            min = 8,
            max = 512,
            message = "Description must be from 8 to 512 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    String description;

    @ArraySchema(arraySchema = @Schema(description = "Ids of tasks, which will be added in the group", example = "[1, 2]"))
    List<Long> taskIds = new ArrayList<>();

}
