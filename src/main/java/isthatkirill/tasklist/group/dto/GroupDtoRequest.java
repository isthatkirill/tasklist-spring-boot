package isthatkirill.tasklist.group.dto;

import isthatkirill.tasklist.validation.OnCreate;
import isthatkirill.tasklist.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class GroupDtoRequest {

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

    List<Long> taskIds;

}
