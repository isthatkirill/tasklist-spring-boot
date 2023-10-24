package isthatkirill.tasklist.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Kirill Emelyanov
 */

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    private final UserDto userDto = UserDto.builder()
            .email("email@email.ru")
            .name("name")
            .username("username")
            .password("password")
            .build();

    @Test
    @SneakyThrows
    void userDtoSerializeTest() {
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result)
                .hasJsonPathStringValue("$.email", userDto.getEmail())
                .hasJsonPathStringValue("$.name", userDto.getName())
                .hasJsonPathStringValue("$.username", userDto.getUsername())
                .hasJsonPathStringValue("$.password", userDto.getPassword());
    }

    @Test
    @SneakyThrows
    void userDtoDeserializeTest() {
        String content = """
                    {
                        "email": "email@email.ru",
                        "name": "name",
                        "username": "username",
                        "password": "password"
                    }
                    """;
        UserDto deserialized = json.parse(content).getObject();

        assertThat(deserialized)
                .hasFieldOrPropertyWithValue("email", userDto.getEmail())
                .hasFieldOrPropertyWithValue("name", userDto.getName())
                .hasFieldOrPropertyWithValue("username", userDto.getUsername())
                .hasFieldOrPropertyWithValue("password", userDto.getPassword());
    }

}