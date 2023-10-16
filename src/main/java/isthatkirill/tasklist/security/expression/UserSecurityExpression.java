package isthatkirill.tasklist.security.expression;

import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.group.model.Group;
import isthatkirill.tasklist.group.repository.GroupRepository;
import isthatkirill.tasklist.security.model.JwtUser;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.task.repository.TaskRepository;
import isthatkirill.tasklist.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Kirill Emelyanov
 */

@Service("userSecurityExpression")
@RequiredArgsConstructor
public class UserSecurityExpression {

    private final TaskRepository taskRepository;
    private final GroupRepository groupRepository;

    public boolean canAccessUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isCorrectUserId(userId) || isAdmin(authentication);
    }

    public boolean canAccessAdminEndpoints() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isAdmin(authentication);
    }

    public boolean isCorrectUserId(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser user = (JwtUser) authentication.getPrincipal();
        Long id = user.getId();
        return userId.equals(id);
    }

    public boolean isTaskOwner(Long taskId, Long userId) {
        return isTaskExists(taskId) && isCorrectUserId(userId) && taskRepository.existsTaskByIdAndAndOwnerId(taskId, userId);
    }

    public boolean isGroupOwner(Long groupId, Long userId) {
        return isGroupExists(groupId) && isCorrectUserId(userId) && groupRepository.existsGroupByIdAndOwnerId(groupId, userId);
    }

    private boolean isGroupExists(Long id) {
        if (!groupRepository.existsById(id)) throw new EntityNotFoundException(Group.class, id);
        return true;
    }

    private boolean isTaskExists(Long id) {
        if (!taskRepository.existsById(id)) throw new EntityNotFoundException(Task.class, id);
        return true;
    }

    private boolean isAdmin(Authentication authentication) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Role.ROLE_ADMIN.name());
        return authentication.getAuthorities().contains(authority);
    }

}
