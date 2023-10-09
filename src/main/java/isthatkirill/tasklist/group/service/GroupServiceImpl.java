package isthatkirill.tasklist.group.service;

import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.group.dto.GroupDtoRequest;
import isthatkirill.tasklist.group.dto.GroupDtoResponse;
import isthatkirill.tasklist.group.mapper.GroupMapper;
import isthatkirill.tasklist.group.model.Group;
import isthatkirill.tasklist.group.repository.GroupRepository;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.task.model.enums.Status;
import isthatkirill.tasklist.task.repository.TaskRepository;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public GroupDtoResponse create(GroupDtoRequest groupDtoRequest, Long userId) {
        User user = checkIfUserExistsAndGet(userId);
        List<Task> tasks = taskRepository.findTasksByIdInAndOwnerId(groupDtoRequest.getTaskIds(), userId);
        checkIfAllTasksFound(tasks.size(), groupDtoRequest.getTaskIds().size());
        Group group = groupMapper.toGroup(groupDtoRequest, tasks, user);
        GroupDtoResponse groupDtoResponse = groupMapper.toGroupDtoResponse(groupRepository.save(group));
        groupDtoResponse.setProgress(computeProgress(tasks));
        log.info("User id={} created new group={}", userId, groupDtoResponse);
        return groupDtoResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDtoResponse getById(Long groupId) {
        Group group = checkIfGroupExistsAndGet(groupId);
        GroupDtoResponse groupDtoResponse = groupMapper.toGroupDtoResponse(group);
        groupDtoResponse.setProgress(computeProgress(group.getTasks()));
        log.info("Get group with id={}", groupId);
        return groupDtoResponse;
    }


    private String computeProgress(List<Task> tasks) {
        return tasks.stream()
                .filter(t -> t.getStatus().equals(Status.DONE.name()))
                .count() + "/" + tasks.size();
    }

    private void checkIfAllTasksFound(int found, int provided) {
        if (found != provided) {
            throw new EntityNotFoundException(Task.class, "Cannot create group: not all tasks were found.");
        }
    }

    private User checkIfUserExistsAndGet(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    private Group checkIfGroupExistsAndGet(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        //TODO ADD COMPUTIMNG PROGRESS
    }

}