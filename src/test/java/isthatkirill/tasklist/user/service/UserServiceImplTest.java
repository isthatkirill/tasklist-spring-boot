package isthatkirill.tasklist.user.service;

import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.error.exception.entity.NotUniqueException;
import isthatkirill.tasklist.mail.model.MailType;
import isthatkirill.tasklist.mail.service.MailService;
import isthatkirill.tasklist.user.dto.UserDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MailService mailService;

    private UserDto userDto = UserDto.builder()
            .email("email@email.ru")
            .name("name")
            .username("username")
            .password("password")
            .build();

    @Test
    @Order(1)
    @Sql(value = {"/scripts/drop.sql", "/scripts/init.sql", "/scripts/users.sql"})
    void createTest() {
        String encodedPassword = userDto.getPassword() + "encoded";

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(encodedPassword);

        UserDto result = userService.create(userDto); // id = 4

        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("email", userDto.getEmail())
                .hasFieldOrPropertyWithValue("name", userDto.getName())
                .hasFieldOrPropertyWithValue("username", userDto.getUsername())
                .hasFieldOrPropertyWithValue("password", encodedPassword);

        verify(passwordEncoder).encode(userDto.getPassword());
        verify(mailService).sendEmail(any(), eq(MailType.REGISTRATION), eq(new Properties()));
    }

    @Test
    @Order(2)
    void createWithSameEmailAndUsernameTest() {
        assertThrows(NotUniqueException.class, () -> userService.create(userDto));

        verifyNoInteractions(passwordEncoder, mailService);
    }

    @Test
    @Order(3)
    void updateTest() {
        Long id = 4L;
        String encodedPassword = userDto.getPassword() + "encoded";

        userDto.setUsername("new username");
        userDto.setName("new name");
        userDto.setEmail("new@email.ru");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(encodedPassword);

        UserDto result = userService.update(userDto, id);

        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("email", userDto.getEmail())
                .hasFieldOrPropertyWithValue("name", userDto.getName())
                .hasFieldOrPropertyWithValue("username", userDto.getUsername())
                .hasFieldOrPropertyWithValue("password", encodedPassword);

        verify(passwordEncoder).encode(userDto.getPassword());
    }

    @Test
    @Order(4)
    void updateWithAlreadyExistentUsernameTest() {
        Long id = 4L;

        userDto.setUsername("isthatkirill");

        assertThrows(NotUniqueException.class, () -> userService.update(userDto, id));

        verifyNoInteractions(passwordEncoder, mailService);
    }

    @Test
    @Order(5)
    void updateNonExistentUserTest() {
        Long id = Long.MAX_VALUE;

        userDto.setUsername("isthatkirill");

        assertThrows(EntityNotFoundException.class, () -> userService.update(userDto, id));

        verifyNoInteractions(passwordEncoder, mailService);
    }

    @Test
    @Order(6)
    void getByIdTest() {
        Long id = 1L;

        UserDto result = userService.getById(id);

        assertThat(result).isNotNull()
                .extracting(UserDto::getUsername)
                .isEqualTo("isthatkirill");
    }

    @Test
    @Order(7)
    void getByIdNonExistentTest() {
        Long id = Long.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> userService.getById(id));
    }

    @Test
    @Order(8)
    void getAllTest() {
        List<UserDto> users = userService.getAll();

        assertThat(users).hasSize(4)
                .extracting(UserDto::getUsername)
                .containsExactlyInAnyOrder("isthatkirill", "offi_anya", "stassy", "new username");
    }


}