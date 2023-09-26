package isthatkirill.tasklist.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

/**
 * @author Kirill Emelyanov
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @NotBlank(message = "Name cannot be blank or null")
    @Length(min = 4, max = 255, message = "Length of the name must be from 4 to 255 characters")
    private String name;

    @NotBlank(message = "Username cannot be blank or null")
    @Length(min = 4, max = 255, message = "Length of the username must be from 4 to 255 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank or null")
    @Email(message = "Email must satisfy pattern")
    private String email;

    @NotBlank(message = "Password cannot be blank or null")
    @Length(min = 4, max = 255, message = "Length of the password must be from 4 to 255 characters")
    private String password;

}
