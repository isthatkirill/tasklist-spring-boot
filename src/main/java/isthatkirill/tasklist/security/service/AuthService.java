package isthatkirill.tasklist.security.service;

import isthatkirill.tasklist.security.dto.JwtRequest;
import isthatkirill.tasklist.security.dto.JwtResponse;

/**
 * @author Kirill Emelyanov
 */

public interface AuthService {

    JwtResponse login(JwtRequest jwtRequest);

    JwtResponse refresh(String refreshToken);

}
