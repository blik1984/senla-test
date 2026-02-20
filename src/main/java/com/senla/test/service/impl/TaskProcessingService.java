package com.senla.test.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.senla.test.beans.Task;
import com.senla.test.beans.TaskStatus;
import com.senla.test.dao.TaskRepository;
import com.senla.test.service.SpellerService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProcessingService {

	private final TaskRepository taskRepository;
	private final SpellerService spellerService;

	@Scheduled(fixedDelay = 5000)
	@Transactional
	public void run() {

		// предположим что у нас один инстанс приложения с одним потоком и нет проблем с
		// дублирующейся обработкой задач в БД
		try {
			List<Task> taskList = taskRepository.findTop10ByStatus(TaskStatus.ACTIV);

			if (!taskList.isEmpty()) {
				log.info("TaskProcessing started. Total={}", taskList.size());
				int completedTask = 0;
				int failedTask = 0;
				for (Task task : taskList) {
					Optional<String> result = spellerService.checkText(task.getText(), task.getLang());
					if (result.isEmpty()) {
						task.setStatus(TaskStatus.PROCESSING_ERROR);
						failedTask++;
					} else {
						task.setText(result.get());
						task.setStatus(TaskStatus.COMPLETED);
						completedTask++;
					}
				}
				log.info("TaskProcessing finished. Total={}, completed={}, failed={}", taskList.size(), completedTask,
						failedTask);
			}
		} catch (Exception e) {
			log.error("TaskProcessing scheduler failed", e);
		}
	}
}
