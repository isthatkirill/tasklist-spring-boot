package isthatkirill.tasklist.security.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Kirill Emelyanov
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtResponse {

    Long id;
    String username;
    String accessToken;
    String refreshToken;

}
