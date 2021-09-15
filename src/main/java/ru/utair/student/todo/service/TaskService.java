package ru.utair.student.todo.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.utair.student.todo.dto.FindParams;
import ru.utair.student.todo.entity.Tag;
import ru.utair.student.todo.entity.Task;
import ru.utair.student.todo.exception.NotFoundException;
import ru.utair.student.todo.repository.TaskRepository;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TagService tagService;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public List<Task> findByParam(FindParams findParams) {
        return taskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            //Сортировка по createDate
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));

            List<Predicate> predicates = new ArrayList<>();

            //Поиск по выполнен/невыполнен
            if (findParams.getIsComplete() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("isComplete"),
                        findParams.getIsComplete()));
            }

            //Поиск Только главных задач
            predicates.add(criteriaBuilder.isNull(root.get("parentId")));

            //Поиск по тегам в формате или
            if (!CollectionUtils.isEmpty(findParams.getTags())){
                Join<Task, Tag> tags = root.join("tags");
                predicates.add(criteriaBuilder.or(
                        findParams.getTags()
                                .stream()
                                .map(tag -> criteriaBuilder.equal(tags.get("id"), tag.getId()))
                                .toArray(Predicate[]::new)
                ));
                criteriaQuery.groupBy(root.get("id"));
            }

            //Поиск по следующим указанной дате
            if (findParams.getAfterCreateDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), findParams.getAfterCreateDate()));
            }

            //Поиск по предыдущим указанной дате
            if (findParams.getBeforeCreateDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), findParams.getBeforeCreateDate()));
            }

            //Поиск по тексту
            if (StringUtils.isNotEmpty(findParams.getText())) {
                Stream.of(findParams
                        .getText()
                        .toUpperCase()
                        .trim()
                        .split("[^a-zA-Za-яA-ЯёЁ\\d]"))
                        .forEach(key -> {
                            if (key.length() >= 3) {
                                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("text")), "%" + key + "%"));
                            }
                        });
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });
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

    public Task addTag(Long idTag, Long idTask) {
        Tag tag = tagService.findById(idTag);
        Task task = findById(idTask);

        if (task.getTags() == null) {
            task.setTags(new HashSet<>());
        }
        task.getTags().add(tag);

        return taskRepository.save(task);
    }
}
