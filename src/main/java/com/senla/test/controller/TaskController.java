package com.senla.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.senla.test.beans.Task;
import com.senla.test.beans.TaskStatus;
import com.senla.test.dto.TaskRequest;
import com.senla.test.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;

	@PostMapping
	public Long createTask(@RequestBody TaskRequest request) {
		return taskService.addTask(request);
	}

	@GetMapping("/{id}/status")
	public TaskStatus getStatus(@PathVariable("id") Long id) {
		System.out.println("контроллер");

		return taskService.checkStatus(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Task> getTask(@PathVariable("id") Long id) {
		Task task = taskService.getTask(id);

		if (task == null) {
			return ResponseEntity.notFound().build(); 
		}

		return ResponseEntity.ok(task); 
	}
}
