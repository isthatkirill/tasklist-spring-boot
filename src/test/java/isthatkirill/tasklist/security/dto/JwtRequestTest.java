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
class JwtRequestTest {

    @Autowired
    private JacksonTester<JwtRequest> json;

    @Test
    @SneakyThrows
    void jwtRequestSerializeTest() {
        JwtRequest request = JwtRequest.builder()
                .username("username")
                .password("password")
                .build();

        JsonContent<JwtRequest> result = json.write(request);

        assertThat(result)
                .hasJsonPathStringValue("username", request.getUsername())
                .hasJsonPathStringValue("password", request.getPassword());
    }

    @Test
    @SneakyThrows
    void jwtRequestDeserializeTest() {
        String content = """
                {
                    "username": "username",
                    "password": "password"
                }
                """;

        JwtRequest result = json.parse(content).getObject();

        assertThat(result)
                .hasFieldOrPropertyWithValue("username", "username")
                .hasFieldOrPropertyWithValue("password", "password");
    }

}