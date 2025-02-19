package com.example.todoapi.presentation.controller;

import com.example.todoapi.presentation.dto.CreateTaskDto;
import com.example.todoapi.presentation.dto.TaskDTO;
import com.example.todoapi.infrastructure.mapper.TaskMapper;
import com.example.todoapi.domain.model.Task;
import com.example.todoapi.application.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task API", description = "Endpoints para gerenciamento de tarefas")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @Operation(
            summary = "Listar todas as tarefas",
            description = "Retorna uma lista de tarefas. Se o parâmetro 'status' for fornecido, filtra as tarefas pelo status."
    )
    @GetMapping
    public ResponseEntity<?> getAllTasks(@RequestParam(value = "status", required = false) String status) {
        List<Task> tasks;
        if (status != null && !status.isEmpty()) {
            tasks = taskService.findTasksByStatus(status);
            if (tasks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhuma tarefa encontrada com o status: " + status);
            }
        } else {
            tasks = taskService.findAllTasks();
            if (tasks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhuma tarefa encontrada.");
            }
        }
        List<TaskDTO> taskDtos = tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskDtos);
    }

    @Operation(
            summary = "Obter uma tarefa por ID",
            description = "Retorna a tarefa correspondente ao ID informado."
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(
            @PathVariable Long id) {
        return taskService.findTaskById(id)
                .map(task -> ResponseEntity.ok(taskMapper.toDTO(task)))
                .orElse(ResponseEntity.notFound().build());
    }



    @Operation(
            summary = "Criar uma nova tarefa",
            description = "Cria uma nova tarefa no sistema e retorna a tarefa criada."
    )
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDto createTaskDto) {
        try {
            Task task = taskMapper.toEntity(createTaskDto);
            Task createdTask = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(taskMapper.toDTO(createdTask));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao criar a tarefa.");
        }
    }

    @Operation(
            summary = "Atualizar uma tarefa",
            description = "Atualiza a tarefa com base no ID informado."
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @Parameter(description = "ID da tarefa a ser atualizada", example = "1")
            @PathVariable Long id,
            @RequestBody TaskDTO taskDTO) {
        try {
            Task task = taskMapper.toEntity(taskDTO);
            Task updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(taskMapper.toDTO(updatedTask));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa com ID " + id + " não encontrada para atualização.");
        }
    }

    @Operation(
            summary = "Excluir uma tarefa",
            description = "Exclui a tarefa com base no ID informado."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @Parameter(description = "ID da tarefa a ser excluída", example = "1")
            @PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa com ID " + id + " não encontrada para exclusão.");
        }
    }
}
