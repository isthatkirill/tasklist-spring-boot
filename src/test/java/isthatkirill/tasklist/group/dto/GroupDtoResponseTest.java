package isthatkirill.tasklist.group.dto;

import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kirill Emelyanov
 */

@JsonTest
@ActiveProfiles("test")
class GroupDtoResponseTest {

    @Autowired
    private JacksonTester<GroupDtoResponse> json;

    private final GroupDtoResponse groupDtoResponse = GroupDtoResponse.builder()
            .id(1L)
            .title("title")
            .description("description")
            .progress("0/1")
            .tasks(List.of(
                    TaskDtoResponse.builder()
                            .id(1L)
                            .title("task_title")
                            .description("task_description")
                            .createdAt(LocalDateTime.of(2023, 2, 15, 13, 12, 11)).build()

            )).build();

    @Test
    @SneakyThrows
    void groupDtoResponseSerializeTest() {
        JsonContent<GroupDtoResponse> result = json.write(groupDtoResponse);

        assertThat(result)
                .hasJsonPathNumberValue("id", groupDtoResponse.getId())
                .hasJsonPathStringValue("title", groupDtoResponse.getTitle())
                .hasJsonPathStringValue("description", groupDtoResponse.getDescription())
                .hasJsonPathStringValue("progress", groupDtoResponse.getProgress())
                .hasJsonPathArrayValue("tasks", groupDtoResponse.getTasks());
    }

    @Test
    @SneakyThrows
    void groupDtoResponseDeserializeTest() {
        String content = """
                {
                    "id": 1,
                    "title": "title",
                    "description": "description",
                    "progress": "0/1",
                    "tasks": [
                        {
                            "id": 1,
                            "title": "task_title",
                            "description": "task_description",
                            "createdAt": "2023-02-15 13:12:11"
                        }
                    ]
                }
                """;

        GroupDtoResponse result = json.parse(content).getObject();

        assertThat(result).isNotNull()
                .isEqualTo(groupDtoResponse);
    }

}