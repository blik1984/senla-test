package com.senla.test.dao;

import java.util.List;

import com.senla.test.beans.Task;
import com.senla.test.beans.TaskStatus;

public interface TaskRepository {
	
	Long addTask(Task task);
	TaskStatus checkStatus (Long id);
	Task getTask(Long id);
	List<Task> findTop10ByStatus(TaskStatus status);
	boolean setTask (Task task);

}
