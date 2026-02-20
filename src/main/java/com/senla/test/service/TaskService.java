package com.senla.test.service;

import com.senla.test.beans.Task;
import com.senla.test.beans.TaskStatus;
import com.senla.test.dto.TaskRequest;

public interface TaskService {
	
	Long addTask(TaskRequest task);
	TaskStatus checkStatus (Long id);
	Task getTask(Long id);

}
