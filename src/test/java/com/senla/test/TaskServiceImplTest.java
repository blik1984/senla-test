package com.senla.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.senla.test.beans.Task;
import com.senla.test.beans.TaskLang;
import com.senla.test.beans.TaskStatus;
import com.senla.test.dao.TaskRepository;
import com.senla.test.dto.TaskRequest;
import com.senla.test.exception.ValidationException;
import com.senla.test.service.impl.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskServiceImpl taskService;

	@Test
	void addTask_shouldCreateTaskAndReturnId_whenRequestIsVald() {
		TaskRequest taskRequest = new TaskRequest();

		taskRequest.setText("мама мыла раму");
		taskRequest.setLang("RU");

		when(taskRepository.addTask(any(Task.class))).thenReturn(10L);

		Long id = taskService.addTask(taskRequest);

		assertEquals(10L, id);

		ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
		verify(taskRepository, times(1)).addTask(captor.capture());

		Task saved = captor.getValue();
		assertEquals("мама мыла раму", saved.getText());
		assertEquals(TaskLang.RU, saved.getLang());
		assertEquals(TaskStatus.ACTIV, saved.getStatus());
	}

	@Test
	void addTask_shouldThrowValidationException_whenRequestIsInvalid() {
		TaskRequest request = new TaskRequest();
		request.setText("ab");
		request.setLang("RU");

		ValidationException ex = assertThrows(ValidationException.class, () -> taskService.addTask(request));

		assertFalse(ex.getErrors().isEmpty());

		verify(taskRepository, never()).addTask(any());
	}

	@Test
	void addTask_shouldPropagateException_whenRepositoryFails() {
		TaskRequest request = new TaskRequest();
		request.setText("valid text");
		request.setLang("EN");

		when(taskRepository.addTask(any(Task.class))).thenThrow(new RuntimeException("DB is down"));

		RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.addTask(request));

		assertEquals("DB is down", ex.getMessage());

		verify(taskRepository, times(1)).addTask(any());
	}
}
