package ru.utair.student.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.utair.student.todo.dto.FindParams;
import ru.utair.student.todo.entity.Task;
import ru.utair.student.todo.exception.NotFoundException;
import ru.utair.student.todo.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/task")
    List<Task> findAll() {
        return clear(taskService.findAll());
    }

    private List<Task> clear(List<Task> tasks) {
        tasks.forEach(this::clear);
        return tasks;
    }

    private Task clear(Task task) {
        if (task.getTags() != null) {
            task.getTags().forEach(tag -> {
                tag.setTaskSet(null);
            });
        }
        return task;
    }

    @GetMapping("/task/{id}")
    Task findById(@PathVariable Long id) {
        return clear(taskService.findById(id));
    }

    @PostMapping("/task")
    Task add(@RequestBody Task task) {
        return clear(taskService.add(task));
    }

    @PutMapping("/task/{id}")
    Task save(@RequestBody Task task, @PathVariable Long id) {
        return  clear(taskService.save(task, id));
    }

    @DeleteMapping("/task/{id}")
    void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PostMapping("/task/{idTask}/addTag/{idTag}")
    public Task addTag(@PathVariable Long idTask, @PathVariable Long idTag) {
        return clear(taskService.addTag(idTag, idTask));
    }

    @PostMapping("/task/findByParam")
    public List<Task> findByParam(@RequestBody FindParams findParams){
        return clear(taskService.findByParam(findParams));
    }
}
