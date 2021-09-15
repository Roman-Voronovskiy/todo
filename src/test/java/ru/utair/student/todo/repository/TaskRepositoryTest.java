package ru.utair.student.todo.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.utair.student.todo.TodoApplication;
import ru.utair.student.todo.dto.FindParams;
import ru.utair.student.todo.entity.Tag;
import ru.utair.student.todo.entity.Task;
import ru.utair.student.todo.service.TaskService;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TodoApplication.class)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TaskService taskService;

    private final Task task = Task.builder()
            .id(100L)
            .text("TASK_TEXT")
            .build();

    @AfterEach
    public void clear() {
        //taskRepository.findAll()
         //           .forEach(task -> taskRepository.delete(task));
        taskRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    public void findTaskByTextIgnoreCaseContaining() {
        taskRepository.save(task);

        List<Task> result = taskRepository.findTaskByTextIgnoreCaseContaining("ask");

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getText(), equalTo(task.getText()));
    }

    @Test
    public void findTaskByTextIgnoreCaseContains() {
        taskRepository.save(task);

        List<Task> result = taskRepository.findTaskByTextIgnoreCaseContains("ask");

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getText(), equalTo(task.getText()));
    }

    @Test
    public void findTaskByTextIgnoreCaseContaining2() {
        Task task2 = Task.builder()
                .id(10L)
                .text("taSk_two")
                .build();
        taskRepository.save(task2);

        List<Task> result = taskRepository.findTaskByTextIgnoreCaseContaining("ask");

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getText(), equalTo(task2.getText()));
    }

    @Test
    public void searchTest() {
        Tag tag = Tag.builder()
                .text("Hello")
                .build();
        tagRepository.save(tag);
        Tag tag2 = Tag.builder()
                .text("Hello2")
                .build();
        tagRepository.save(tag2);
        Task task2 = Task.builder()
                .id(10L)
                .text("taSk_two")
                .tags(new HashSet<>(){{
                    add(tag);
                    add(tag2);
                }})
                .build();
        taskRepository.save(task2);

        List<Task> result = taskService.findByParam(FindParams.builder()
                .tags(Collections.singletonList(tag))
                .build());

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getText(), equalTo(task2.getText()));
    }

    @Test
    public void searchTest2() {
        Tag tag = Tag.builder()
                .text("Hello")
                .build();
        tagRepository.save(tag);
        Tag tag2 = Tag.builder()
                .text("Hello2")
                .build();
        tagRepository.save(tag2);
        Task task2 = Task.builder()
                .id(10L)
                .text("taSk_two")
                .tags(new HashSet<>(){{
                    add(tag);
                    add(tag2);
                }})
                .build();
        taskRepository.save(task2);

        List<Task> result = taskService.findByParam(FindParams.builder()
                .tags(Collections.singletonList(tag2))
                .build());

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getText(), equalTo(task2.getText()));
    }

    @Test
    public void searchTest3() {
        Tag tag = Tag.builder()
                .text("Hello")
                .build();
        tagRepository.save(tag);
        Tag tag2 = Tag.builder()
                .text("Hello2")
                .build();
        tagRepository.save(tag2);
        Task task2 = Task.builder()
                .id(3L)
                .text("taSk_two")
                .tags(new HashSet<>(){{
                    add(tag);
                    add(tag2);
                }})
                .build();
        taskRepository.save(task2);

        List<Task> result = taskService.findByParam(FindParams.builder()
                .tags(new ArrayList<Tag>(){{
                    add(tag);
                    add(tag2);
                }})
                .build());

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getText(), equalTo(task2.getText()));
    }



}
