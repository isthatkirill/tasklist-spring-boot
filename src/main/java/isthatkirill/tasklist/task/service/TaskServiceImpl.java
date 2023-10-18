package isthatkirill.tasklist.task.service;

import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.task.dto.TaskDtoRequest;
import isthatkirill.tasklist.task.dto.TaskDtoResponse;
import isthatkirill.tasklist.task.mapper.TaskMapper;
import isthatkirill.tasklist.task.model.Task;
import isthatkirill.tasklist.task.repository.TaskRepository;
import isthatkirill.tasklist.user.model.User;
import isthatkirill.tasklist.user.repositorty.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    public TaskDtoResponse create(TaskDtoRequest taskDto, Long userId) {
        log.info("New task created {} by user id={}", taskDto, userId);
        User user = checkIfUserExistsAndGet(userId);
        Task task = taskMapper.toTask(taskDto, user);
        return taskMapper.toTaskDtoResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDtoResponse update(TaskDtoRequest taskDto, Long userId, Long taskId) {
        log.info("Task id={} updated by user id={}. updated task = {}", taskId, userId, taskDto);
        Task task = checkIfTaskExistsAndGet(taskId);
        if (taskDto.getNotify() != null) task.setNotify(taskDto.getNotify());
        if (taskDto.getTitle() != null) task.setTitle(taskDto.getTitle());
        if (taskDto.getDescription() != null) task.setDescription(taskDto.getDescription());
        if (taskDto.getPriority() != null) task.setPriority(taskDto.getPriority());
        if (taskDto.getStatus() != null) task.setStatus(taskDto.getStatus());
        if (taskDto.getExpiresAt() != null) task.setExpiresAt(taskDto.getExpiresAt());
        task.setLastModifiedAt(LocalDateTime.now());
        return taskMapper.toTaskDtoResponse(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDtoResponse getById(Long taskId) {
        log.info("Get task id={}", taskId);
        return taskMapper.toTaskDtoResponse(checkIfTaskExistsAndGet(taskId));
    }


    @Override
    @Transactional
    public void delete(Long taskId) {
        taskRepository.deleteById(taskId);
        log.info("Task id={} deleted", taskId);
    }

    @Override
    @Transactional
    public List<TaskDtoResponse> getAll(Long userId, String keyword, String priority, String status,
                                        LocalDateTime expiresBefore, Boolean notify, Integer from, Integer size) {
        log.info("User id={} requested all his tasks with filtering params: keyword={}, priority={}, status={}, " +
                "expiresBefore={}, notify={}, from={}, size={}", userId, keyword, priority, status, expiresBefore,
                notify, from, size);
        return taskMapper.toTaskDtoResponse(
                taskRepository.getAllTasks(userId, keyword, priority, status, expiresBefore, notify, from, size)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllSoonTasks(Duration duration) {
        return taskRepository.findAllSoonTasks(Timestamp.valueOf(LocalDateTime.now().plus(duration)));
    }

    private Task checkIfTaskExistsAndGet(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, id));
    }

    private User checkIfUserExistsAndGet(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

}
