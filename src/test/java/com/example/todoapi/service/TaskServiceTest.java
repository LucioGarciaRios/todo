package com.example.todoapi.service;

import com.example.todoapi.application.service.TaskService;
import com.example.todoapi.domain.model.Task;
import com.example.todoapi.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        task = Task.builder()
                .id(1L)
                .title("Teste Task")
                .description("Descrição da task")
                .createdAt(LocalDateTime.now())
                .status("pendente")
                .build();
    }

    @Test
    public void testFindAllTasks() {
        List<Task> tasks = Arrays.asList(task);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.findAllTasks();

        assertEquals(1, result.size());
        assertEquals("Teste Task", result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testFindTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.findTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("Teste Task", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task created = taskService.createTask(task);

        assertNotNull(created);
        assertEquals("Teste Task", created.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testUpdateTask() {
        Task updatedTask = Task.builder()
                .title("Task Atualizada")
                .description("Descrição atualizada")
                .status("completa")
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("Task Atualizada", result.getTitle());
        assertEquals("completa", result.getStatus());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTaskNotFound() {
        Task updatedTask = Task.builder()
                .title("Task Atualizada")
                .description("Descrição atualizada")
                .status("completa")
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(1L, updatedTask);
        });

        assertEquals("Task not found", exception.getMessage());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTaskNotFound() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}
