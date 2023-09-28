package isthatkirill.tasklist.task.service;

import isthatkirill.tasklist.error.exception.EntityNotFoundException;
import isthatkirill.tasklist.task.dto.UserTaskDto;
import isthatkirill.tasklist.task.mapper.TaskMapper;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.task.repository.TaskRepository;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public UserTaskDto create(UserTaskDto taskDto, Long userId) {
        log.info("New task created {} by user id={}", taskDto, userId);
        User user = checkIfUserExistsAndGet(userId);
        Task task = taskMapper.toTask(taskDto, user);
        return taskMapper.toUserTaskDto(taskRepository.save(task));
    }

    private User checkIfUserExistsAndGet(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

}
