package ru.utair.student.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.utair.student.todo.entity.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    public List<Task> findTaskByTextIgnoreCaseContaining(String subText);
    public List<Task> findTaskByTextIgnoreCaseContains(String subText);

}
