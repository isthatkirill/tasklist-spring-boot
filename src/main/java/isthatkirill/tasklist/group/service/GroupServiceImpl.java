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
    @Transactional
    public GroupDtoResponse update(GroupDtoRequest groupDtoRequest, Long groupId, Long userId) {
        Group group = checkIfGroupExistsAndGet(groupId);
        if (groupDtoRequest.getTitle() != null) {
            group.setTitle(groupDtoRequest.getTitle());
        }
        if (groupDtoRequest.getDescription() != null) {
            group.setDescription(groupDtoRequest.getDescription());
        }
        List<Task> tasks = taskRepository.findTasksByIdInAndOwnerId(groupDtoRequest.getTaskIds(), userId);
        checkIfAllTasksFound(tasks.size(), groupDtoRequest.getTaskIds().size());
        group.setTasks(tasks);
        GroupDtoResponse groupDtoResponse = groupMapper.toGroupDtoResponse(groupRepository.save(group));
        groupDtoResponse.setProgress(computeProgress(tasks));
        log.info("User id={} updated group={}", userId, groupDtoResponse);
        return groupDtoResponse;
    }


    @Override
    @Transactional
    public GroupDtoResponse addTaskInGroup(Long userId, Long groupId, Long taskId) {
        Group group = checkIfGroupExistsAndGet(groupId);
        Task joinTask = checkIfTaskExistsAndGet(taskId);
        List<Task> tasks = group.getTasks();
        checkIfAlreadyExists(tasks, joinTask);
        tasks.add(joinTask);
        group.setTasks(tasks);
        GroupDtoResponse groupDtoResponse = groupMapper.toGroupDtoResponse(groupRepository.save(group));
        groupDtoResponse.setProgress(computeProgress(tasks));
        log.info("User id={} added task id={} in group id={}", userId, taskId, groupId);
        return groupDtoResponse;
    }

    @Override
    @Transactional
    public GroupDtoResponse deleteTaskFromGroup(Long userId, Long groupId, Long taskId) {
        Group group = checkIfGroupExistsAndGet(groupId);
        Task taskToDel = checkIfTaskExistsAndGet(taskId);
        List<Task> tasks = group.getTasks();
        tasks.remove(taskToDel);
        group.setTasks(tasks);
        GroupDtoResponse groupDtoResponse = groupMapper.toGroupDtoResponse(groupRepository.save(group));
        groupDtoResponse.setProgress(computeProgress(tasks));
        log.info("User id={} deleted task id={} from group id={}", userId, taskId, groupId);
        return groupDtoResponse;
    }

    @Override
    @Transactional
    public void delete(Long groupId) {
        checkIfGroupExistsAndGet(groupId);
        groupRepository.deleteById(groupId);
        log.info("Group id={} deleted", groupId);
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

    private void checkIfAlreadyExists(List<Task> tasks, Task joinTask) {
        if (tasks.contains(joinTask)) throw new IllegalStateException("Task already exists in current group.");
    }

    private User checkIfUserExistsAndGet(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    private Group checkIfGroupExistsAndGet(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    private Task checkIfTaskExistsAndGet(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, id));
    }

}
