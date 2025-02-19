package com.example.todoapi.infrastructure.mapper;

import com.example.todoapi.presentation.dto.CreateTaskDto;
import com.example.todoapi.presentation.dto.TaskDTO;
import com.example.todoapi.domain.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getStatus()
        );
    }

    public Task toEntity(TaskDTO dto) {
        if (dto == null) {
            return null;
        }

        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .id(dto.getId())
                .build();
    }

    public Task toEntity(CreateTaskDto dto) {
        if (dto == null) {
            return null;
        }

        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .build();

    }
}
