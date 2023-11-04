package isthatkirill.tasklist.task.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kirill Emelyanov
 */

@JsonTest
@ActiveProfiles("test")
class TaskDtoResponseTest {

    @Autowired
    private JacksonTester<TaskDtoResponse> json;

    private final TaskDtoResponse taskDtoRequest = TaskDtoResponse.builder()
            .id(1L)
            .title("title")
            .description("description")
            .status("NEW")
            .priority("LOW")
            .notify(true)
            .createdAt(LocalDateTime.now())
            .lastModifiedAt(LocalDateTime.now().plusHours(1))
            .expiresAt(LocalDateTime.now().plusHours(2))
            .build();

    @Test
    @SneakyThrows
    void taskDtoResponseSerializeTest() {
        JsonContent<TaskDtoResponse> result = json.write(taskDtoRequest);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", taskDtoRequest.getId())
                .hasJsonPathStringValue("$.title", taskDtoRequest.getTitle())
                .hasJsonPathStringValue("$.description", taskDtoRequest.getDescription())
                .hasJsonPathStringValue("$.status", taskDtoRequest.getStatus())
                .hasJsonPathStringValue("$.priority", taskDtoRequest.getPriority())
                .hasJsonPathBooleanValue("$.notify", taskDtoRequest.getNotify())
                .hasJsonPathStringValue("$.createdAt", taskDtoRequest.getCreatedAt())
                .hasJsonPathStringValue("$.lastModifiedAt", taskDtoRequest.getLastModifiedAt())
                .hasJsonPathStringValue("$.expiresAt", taskDtoRequest.getExpiresAt());
    }

    @Test
    @SneakyThrows
    void taskDtoResponseDeserializeTest() {
        String content = """
                {
                "id": 1,
                "title": "title",
                "description": "description",
                "status": "NEW",
                "priority": "LOW",
                "notify": true,
                "createdAt": "2022-02-15 13:12:11",
                "lastModifiedAt": "2023-02-15 13:12:11",
                "expiresAt": "2024-02-15 13:12:11"
                }
                """;
        TaskDtoResponse deserialized = json.parse(content).getObject();

        assertThat(deserialized).isNotNull()
                .hasFieldOrPropertyWithValue("title", taskDtoRequest.getTitle())
                .hasFieldOrPropertyWithValue("description", taskDtoRequest.getDescription())
                .hasFieldOrPropertyWithValue("status", taskDtoRequest.getStatus())
                .hasFieldOrPropertyWithValue("priority", taskDtoRequest.getPriority())
                .hasFieldOrPropertyWithValue("notify", taskDtoRequest.getNotify())
                .hasFieldOrProperty("createdAt")
                .hasFieldOrProperty("lastModifiedAt")
                .hasFieldOrProperty("expiresAt");
    }

}