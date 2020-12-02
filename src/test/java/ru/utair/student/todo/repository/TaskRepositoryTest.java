package ru.utair.student.todo.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.utair.student.todo.TodoApplication;
import ru.utair.student.todo.entity.Task;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TodoApplication.class)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private final Task task = Task.builder()
            .id(100L)
            .text("TASK_TEXT")
            .build();

    @AfterEach
    public void clear() {
        taskRepository.findAll()
                    .forEach(task -> taskRepository.delete(task));
    }

    @Test
    public void findTaskByTextIgnoreCaseContaining() {
        taskRepository.save(task);

        List<Task> result = taskRepository.findTaskByTextIgnoreCaseContaining("ask");

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getText(), equalTo(task.getText()));
    }

}
