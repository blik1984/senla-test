package com.senla.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.senla.test.beans.Task;
import com.senla.test.beans.TaskLang;
import com.senla.test.beans.TaskStatus;
import com.senla.test.dao.TaskRepository;
import com.senla.test.service.impl.TaskProcessingService;
import com.senla.test.service.SpellerService;

@ExtendWith(MockitoExtension.class)
public class TaskProcessingServiceTest {

	@Mock
	private TaskRepository taskRepository;
	@Mock
	private SpellerService spellerService;
	@InjectMocks
	private TaskProcessingService service;

	@Test
	void run_shouldCallRepoOneTimes() {

		when(taskRepository.findTop10ByStatus(TaskStatus.ACTIV)).thenReturn(new ArrayList<Task>());
		service.run();
		verify(taskRepository, times(1)).findTop10ByStatus(TaskStatus.ACTIV);
	}

	@Test
	void run_shouldCallRepoOneTimesAndCallSpellerForEachTaskAndStatusCompleted() {
		List<Task> list = new ArrayList<>();
		list.add(makeTask());
		list.add(makeTask());
		when(taskRepository.findTop10ByStatus(TaskStatus.ACTIV)).thenReturn(list);
		when(spellerService.checkText(anyString(), any())).thenReturn(Optional.of("string"));
		service.run();
		verify(taskRepository, times(1)).findTop10ByStatus(TaskStatus.ACTIV);
		verify(spellerService, times(list.size())).checkText(anyString(), any());
		assertEquals(TaskStatus.COMPLETED, list.get(0).getStatus());
	}

	@Test
	void run_changeStatusToProcessingError() {
		List<Task> list = new ArrayList<>();
		list.add(makeTask());
		when(taskRepository.findTop10ByStatus(TaskStatus.ACTIV)).thenReturn(list);
		when(spellerService.checkText(anyString(), any())).thenReturn(Optional.empty());
		
		service.run();
		
		assertEquals(TaskStatus.PROCESSING_ERROR, list.get(0).getStatus());
	}

	private Task makeTask() {
		Task task = new Task();
		task.setText("текст");
		task.setLang(TaskLang.RU);
		task.setStatus(TaskStatus.ACTIV);
		return task;
	}
}
