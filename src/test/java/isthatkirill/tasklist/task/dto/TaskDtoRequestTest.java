package isthatkirill.tasklist.task.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kirill Emelyanov
 */

@JsonTest
class TaskDtoRequestTest {

    @Autowired
    private JacksonTester<TaskDtoRequest> json;

    private final TaskDtoRequest taskDtoRequest = TaskDtoRequest.builder()
            .title("title")
            .description("description")
            .status("NEW")
            .priority("LOW")
            .notify(true)
            .expiresAt(LocalDateTime.now().plusHours(2))
            .build();

    @Test
    @SneakyThrows
    void taskDtoRequestSerializeTest() {
        JsonContent<TaskDtoRequest> result = json.write(taskDtoRequest);

        assertThat(result)
                .hasJsonPathStringValue("$.title", taskDtoRequest.getTitle())
                .hasJsonPathStringValue("$.description", taskDtoRequest.getDescription())
                .hasJsonPathStringValue("$.status", taskDtoRequest.getStatus())
                .hasJsonPathStringValue("$.priority", taskDtoRequest.getPriority())
                .hasJsonPathBooleanValue("$.notify", taskDtoRequest.getNotify())
                .hasJsonPathStringValue("$.expiresAt", taskDtoRequest.getExpiresAt());
    }

    @Test
    @SneakyThrows
    void taskDtoRequestDeserializeTest() {
        String content = """
                {
                "title": "title",
                "description": "description",
                "status": "NEW",
                "priority": "LOW",
                "notify": true,
                "expiresAt": "2024-02-15 13:12:11"
                }
                """;
        TaskDtoRequest deserialized = json.parse(content).getObject();

        assertThat(deserialized).isNotNull()
                .hasFieldOrPropertyWithValue("title", taskDtoRequest.getTitle())
                .hasFieldOrPropertyWithValue("description", taskDtoRequest.getDescription())
                .hasFieldOrPropertyWithValue("status", taskDtoRequest.getStatus())
                .hasFieldOrPropertyWithValue("priority", taskDtoRequest.getPriority())
                .hasFieldOrPropertyWithValue("notify", taskDtoRequest.getNotify())
                .hasFieldOrProperty("expiresAt");
    }

}