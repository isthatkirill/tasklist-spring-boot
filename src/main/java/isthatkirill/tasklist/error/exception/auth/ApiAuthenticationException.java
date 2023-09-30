package isthatkirill.tasklist.error.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Kirill Emelyanov
 */

@Getter
public class ApiAuthenticationException extends AuthenticationException {

    private final HttpStatus httpStatus;

    public ApiAuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}

