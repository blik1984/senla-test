package com.senla.test.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.senla.test.beans.Task;
import com.senla.test.beans.TaskStatus;
import com.senla.test.dao.TaskRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Long addTask(Task task) {

		entityManager.persist(task);
		entityManager.flush();
		return task.getId();
	}

	@Override
	public TaskStatus checkStatus(Long id) {
		Task task = entityManager.find(Task.class, id);
		if (task != null && task.getStatus() != null) {
			return task.getStatus();
		}
		return null;
	}

	@Override
	public Task getTask(Long id) {
		return entityManager.find(Task.class, id);
	}

	@Override
	public List<Task> findTop10ByStatus(TaskStatus status) {

		List<Task> result = entityManager
				.createQuery("select t from Task t where t.status = :status order by t.id desc", Task.class)
				.setParameter("status", status).setMaxResults(10).getResultList();
		return result;
	}

	@Override
	public boolean setTask(Task task) {

		if (task.getId() == null) {
			return false;
		}
		Task result = entityManager.find(Task.class, task.getId());
		if (result == null) {
			return false;
		}
		result.setLang(task.getLang());
		result.setStatus(task.getStatus());
		result.setText(task.getText());
		return true;
	}
}
