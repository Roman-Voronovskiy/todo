package ru.utair.student.todo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
@JsonInclude(JsonInclude.Include.NON_EMPTY)

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(exclude = "taskSet")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    @ManyToMany(mappedBy = "tags")
    private Set<Task> taskSet;
}
