package ru.utair.student.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.utair.student.todo.entity.Task;
import ru.utair.student.todo.exception.NotFoundException;
import ru.utair.student.todo.repository.TaskRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Task add(Task task) {
        task.setCreateDate(new Date());
        task.setUpdateDate(null);
        return taskRepository.save(task);
    }

    public Task save(Task task, Long id) {
        Task saveTask = findById(id);

        if (task.getText() != null) {
            saveTask.setText(task.getText());
        }
        if (task.getIsComplete() != null) {
            saveTask.setIsComplete(task.getIsComplete());
        }
        saveTask.setUpdateDate(new Date());
        return taskRepository.save(saveTask);
    }

    public void delete(Long id) {
        Task task = findById(id);
        taskRepository.delete(task);
    }
}
