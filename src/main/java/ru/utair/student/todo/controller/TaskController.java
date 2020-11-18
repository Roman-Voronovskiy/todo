package ru.utair.student.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
        return taskService.findAll();
    }

    @GetMapping("/task/{id}")
    Task findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping("/task")
    Task add(@RequestBody Task task) {
        return taskService.add(task);
    }

    @PutMapping("/task/{id}")
    Task save(@RequestBody Task task, @PathVariable Long id) {
        return  taskService.save(task, id);
    }

    @DeleteMapping("/task/{id}")
    void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
