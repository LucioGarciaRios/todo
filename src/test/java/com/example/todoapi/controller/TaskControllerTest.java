package com.example.todoapi.controller;

import com.example.todoapi.presentation.dto.CreateTaskDto;
import com.example.todoapi.presentation.dto.TaskDTO;
import com.example.todoapi.infrastructure.mapper.TaskMapper;
import com.example.todoapi.domain.model.Task;
import com.example.todoapi.application.service.TaskService;
import com.example.todoapi.presentation.controller.TaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskController taskController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    private Task mockTask() {
        return new Task(1L, "Teste Task", "Descrição da task", LocalDateTime.now(), "pendente");
    }

    private TaskDTO mockTaskDTO() {
        return new TaskDTO(1L, "Teste Task", "Descrição da task", LocalDateTime.now(), "pendente");
    }

    private CreateTaskDto mockCreateTaskDto() {
        return new CreateTaskDto("Teste Task", "Descrição da task", "pendente");
    }

    @Test
    public void testGetAllTasks() throws Exception {
        when(taskService.findAllTasks()).thenReturn(Collections.singletonList(mockTask()));
        when(taskMapper.toDTO(any(Task.class))).thenReturn(mockTaskDTO());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Teste Task"));

        verify(taskService, times(1)).findAllTasks();
        verify(taskMapper, times(1)).toDTO(any(Task.class));
    }

    @Test
    public void testGetTaskById_Success() throws Exception {
        when(taskService.findTaskById(1L)).thenReturn(Optional.of(mockTask()));
        when(taskMapper.toDTO(any(Task.class))).thenReturn(mockTaskDTO());

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Teste Task"));

        verify(taskService, times(1)).findTaskById(1L);
        verify(taskMapper, times(1)).toDTO(any(Task.class));
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        when(taskService.findTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).findTaskById(1L);
        verify(taskMapper, never()).toDTO(any(Task.class));
    }

    @Test
    public void testCreateTask() throws Exception {
        when(taskMapper.toEntity(any(CreateTaskDto.class))).thenReturn(mockTask());
        when(taskService.createTask(any(Task.class))).thenReturn(mockTask());
        when(taskMapper.toDTO(any(Task.class))).thenReturn(mockTaskDTO());

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCreateTaskDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Teste Task"));

        verify(taskMapper, times(1)).toEntity(any(CreateTaskDto.class));
        verify(taskService, times(1)).createTask(any(Task.class));
        verify(taskMapper, times(1)).toDTO(any(Task.class));
    }

    @Test
    public void testUpdateTask_Success() throws Exception {
        Task updatedTask = new Task(1L, "Updated Task", "Updated description", LocalDateTime.now(), "completed");
        TaskDTO updatedTaskDTO = new TaskDTO(1L, "Updated Task", "Updated description", LocalDateTime.now(), "completed");

        when(taskMapper.toEntity(any(TaskDTO.class))).thenReturn(updatedTask);
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(updatedTaskDTO);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.status").value("completed"));

        verify(taskMapper, times(1)).toEntity(any(TaskDTO.class));
        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
        verify(taskMapper, times(1)).toDTO(any(Task.class));
    }

    @Test
    public void testUpdateTask_NotFound() throws Exception {
        TaskDTO updatedTaskDTO = new TaskDTO(1L, "Updated Task", "Updated description", LocalDateTime.now(), "completed");

        when(taskMapper.toEntity(any(TaskDTO.class))).thenReturn(mockTask());
        when(taskService.updateTask(eq(1L), any(Task.class))).thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskDTO)))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(taskService, times(1)).deleteTask(1L);
    }
}
