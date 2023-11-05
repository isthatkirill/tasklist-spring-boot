package isthatkirill.tasklist.security.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kirill Emelyanov
 */

@JsonTest
@ActiveProfiles("test")
class JwtResponseTest {

    @Autowired
    private JacksonTester<JwtResponse> json;

    @Test
    @SneakyThrows
    void jwtResponseSerializeTest() {
        JwtResponse response = JwtResponse.builder()
                .id(1L)
                .username("username")
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        JsonContent<JwtResponse> result = json.write(response);

        assertThat(result)
                .hasJsonPathNumberValue("id", response.getId())
                .hasJsonPathStringValue("username", response.getUsername())
                .hasJsonPathStringValue("accessToken", response.getAccessToken())
                .hasJsonPathStringValue("refreshToken", response.getRefreshToken());
    }

    @Test
    @SneakyThrows
    void jwtResponseDeserializeTest() {
        String content = """
                {
                    "id": 1,
                    "username": "username",
                    "accessToken": "access",
                    "refreshToken": "refresh"
                }
                """;

        JwtResponse result = json.parse(content).getObject();

        assertThat(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("username", "username")
                .hasFieldOrPropertyWithValue("accessToken", "access")
                .hasFieldOrPropertyWithValue("refreshToken", "refresh");
    }

}