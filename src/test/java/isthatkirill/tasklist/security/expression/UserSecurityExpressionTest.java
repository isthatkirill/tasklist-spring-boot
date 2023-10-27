package isthatkirill.tasklist.security.expression;

import isthatkirill.tasklist.group.repository.GroupRepository;
import isthatkirill.tasklist.security.model.JwtUser;
import isthatkirill.tasklist.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserSecurityExpressionTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private GroupRepository groupRepository;

    @Autowired
    @Qualifier("userSecurityExpression")
    private UserSecurityExpression securityExpression;

    @Test
    void accessByCorrectUserIdTest() {
        Long userId = 1L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = securityExpression.canAccessUser(userId);

        assertThat(result).isTrue();
        verify(authentication).getPrincipal();
    }

    @Test
    void accessByIncorrectUserIdTest() {
        Long userId = 1L;
        Long authUserId = 2L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = securityExpression.canAccessUser(authUserId);

        assertThat(result).isFalse();
        verify(authentication).getPrincipal();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void accessByAdminRoleTest() {
        boolean result = securityExpression.canAccessAdminEndpoints();
        assertThat(result).isTrue();
    }

    @Test
    @WithMockUser
    void accessByNonAdminRoleTest() {
        boolean result = securityExpression.canAccessAdminEndpoints();
        assertThat(result).isFalse();
    }

    @Test
    void isTaskOwnerTest() {
        Long userId = 1L;
        Long taskId = 1L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(taskRepository.existsTaskByIdAndAndOwnerId(any(), any())).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = securityExpression.isTaskOwner(taskId, userId);

        assertThat(result).isTrue();
        verify(authentication).getPrincipal();
        verify(taskRepository).existsTaskByIdAndAndOwnerId(taskId, userId);
    }

    @Test
    void isTaskOwnerFalseTest() {
        Long userId = 1L;
        Long taskId = 1L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(taskRepository.existsTaskByIdAndAndOwnerId(any(), any())).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = securityExpression.isTaskOwner(taskId, userId);

        assertThat(result).isFalse();
        verify(authentication).getPrincipal();
        verify(taskRepository).existsTaskByIdAndAndOwnerId(taskId, userId);
    }

    @Test
    void isGroupOwnerTest() {
        Long userId = 1L;
        Long groupId = 1L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(groupRepository.existsGroupByIdAndOwnerId(any(), any())).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = securityExpression.isGroupOwner(groupId, userId);

        assertThat(result).isTrue();
        verify(authentication).getPrincipal();
        verify(groupRepository).existsGroupByIdAndOwnerId(groupId, userId);
    }

    @Test
    void isGroupOwnerWithIncorrectUserIdTest() {
        Long userId = 1L;
        Long groupId = 1L;
        Long authUserId = 2L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(groupRepository.existsGroupByIdAndOwnerId(any(), any())).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = securityExpression.isGroupOwner(groupId, authUserId);

        assertThat(result).isFalse();
        verify(authentication).getPrincipal();
        verifyNoInteractions(groupRepository);
    }

    @Test
    void isGroupOwnerFalseTest() {
        Long userId = 1L;
        Long groupId = 1L;
        JwtUser user = JwtUser.builder().id(userId).build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(groupRepository.existsGroupByIdAndOwnerId(any(), any())).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean result = securityExpression.isGroupOwner(groupId, userId);

        assertThat(result).isFalse();
        verify(authentication).getPrincipal();
        verify(groupRepository).existsGroupByIdAndOwnerId(groupId, userId);
    }
}