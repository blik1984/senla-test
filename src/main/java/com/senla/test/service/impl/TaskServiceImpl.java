package com.senla.test.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senla.test.beans.Task;
import com.senla.test.beans.TaskLang;
import com.senla.test.beans.TaskStatus;
import com.senla.test.dao.TaskRepository;
import com.senla.test.dto.TaskRequest;
import com.senla.test.exception.TaskNotFoundException;
import com.senla.test.exception.ValidationException;
import com.senla.test.service.TaskService;
import com.senla.test.validation.TaskValidation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;

	@Override
	@Transactional
	public Long addTask(TaskRequest taskRequest) {

		List<String> errors = TaskValidation.validation(taskRequest);
		if (!errors.isEmpty()) {
			log.warn("Text validation failed: {}", errors);
			throw new ValidationException(errors);
		}

		Task task = new Task();
		task.setText(taskRequest.getText());
		task.setLang(TaskLang.valueOf(taskRequest.getLang()));
		task.setStatus(TaskStatus.ACTIV);
		Long id = taskRepository.addTask(task);
		log.info("Added task id={}", id);
		return id;
	}

	@Override
	@Transactional
	public TaskStatus checkStatus(Long id) {
		return taskRepository.checkStatus(id);
	}

	@Override
	@Transactional
	public Task getTask(Long id) {
		Task task = taskRepository.getTask(id);
		if(task == null) {
			throw new TaskNotFoundException("Task with id: " + id + " not found");
		}
		return task;
	}
}
