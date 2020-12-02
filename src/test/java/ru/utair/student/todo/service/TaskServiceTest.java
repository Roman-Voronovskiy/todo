package ru.utair.student.todo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.utair.student.todo.entity.Task;
import ru.utair.student.todo.exception.NotFoundException;
import ru.utair.student.todo.repository.TaskRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private final Task task = Task.builder()
                                .id(100L)
                                .text("TASK_TEXT")
                                .build();

    @Test
    public void findAll() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));

        List<Task> result = taskService.findAll();

        verify(taskRepository, times(1)).findAll();
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), equalTo(task.getId()));
        assertThat(result.get(0).getText(), equalTo(task.getText()));
    }

    @Test
    public void findById() {
        when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));

        Task result = taskService.findById(task.getId());

        verify(taskRepository, times(1)).findById(eq(task.getId()));
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(task.getId()));
        assertThat(result.getText(), equalTo(task.getText()));
    }

    @Test()
    public void findById_NotFoundException() {
        when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            taskService.findById(task.getId());
        });
    }

    @Test
    public void add() {
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());

        Task result = taskService.add(task);

        verify(taskRepository, times(1)).save(any(Task.class));
        assertThat(result.getId(), equalTo(task.getId()));
        assertThat(result.getText(), equalTo(task.getText()));
        assertThat(result.getCreateDate(), notNullValue());
        assertThat(result.getUpdateDate(), nullValue());
    }

    @Test
    public void save() {
        when(taskRepository.save(any(Task.class))).thenAnswer(returnsFirstArg());
        when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));

        Task newTask = Task.builder()
                            .text("TASK_TEXT_200")
                            .build();
        Task result = taskService.save(newTask, task.getId());

        verify(taskRepository, times(1)).save(any(Task.class));
        assertThat(result.getId(), equalTo(task.getId()));
        assertThat(result.getText(), equalTo(newTask.getText()));
        assertThat(result.getUpdateDate(), notNullValue());
    }

    @Test
    public void delete() {
        when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));

        taskService.delete(task.getId());

        verify(taskRepository, times(1)).delete(any(Task.class));
    }


}
