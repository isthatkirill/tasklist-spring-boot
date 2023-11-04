package isthatkirill.tasklist.group.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kirill Emelyanov
 */

@JsonTest
@ActiveProfiles("test")
class GroupDtoRequestTest {

    @Autowired
    private JacksonTester<GroupDtoRequest> json;

    private final GroupDtoRequest groupDtoRequest = GroupDtoRequest.builder()
            .title("title")
            .description("description")
            .taskIds(List.of(1L, 2L))
            .build();

    @Test
    @SneakyThrows
    void groupDtoRequestSerializeTest() {
        JsonContent<GroupDtoRequest> result = json.write(groupDtoRequest);

        assertThat(result)
                .hasJsonPathStringValue("title", groupDtoRequest.getTitle())
                .hasJsonPathStringValue("description", groupDtoRequest.getDescription())
                .hasJsonPathArrayValue("taskIds", groupDtoRequest.getTaskIds());
    }

    @Test
    @SneakyThrows
    void groupDtoRequestDeserializeTest() {
        String content = """
                {
                    "title": "title",
                    "description": "description",
                    "taskIds": [1, 2]
                }
                """;

        GroupDtoRequest result = json.parse(content).getObject();

        assertThat(result).isNotNull()
                .isEqualTo(groupDtoRequest);
    }

}