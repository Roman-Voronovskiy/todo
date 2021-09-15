package ru.utair.student.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.utair.student.todo.entity.Tag;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindParams {
    private String text;
    private Date afterCreateDate;
    private Date beforeCreateDate;
    private List<Tag> tags;
    private Boolean isComplete;
}
