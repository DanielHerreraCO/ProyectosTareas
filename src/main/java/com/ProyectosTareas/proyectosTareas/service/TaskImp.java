package com.ProyectosTareas.proyectosTareas.service;

import com.ProyectosTareas.proyectosTareas.domain.Task;
import com.ProyectosTareas.proyectosTareas.dao.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskImp implements TaskInterface{

    private final TaskDao taskDao;

    @Autowired
    public TaskImp(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public List<Task> listTask() {
        return (List<Task>) taskDao.findAll();
    }

    @Override
    public void saveTask(Task task) {
        taskDao.save(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskDao.delete(task);
    }

    @Override
    public Task findTask(Task task) {
        Long id = Long.valueOf(task.getId());
        return taskDao.findById(id).orElse(null);
    }

    @Override
    public Task findByIdTask(int id) {
        Long ids = Long.valueOf(id);
        Optional<Task> optionalTask = taskDao.findById(ids);
        if (optionalTask.isPresent()) {
            return optionalTask.get();
        } else {
            throw new IllegalArgumentException("Tarea no encontrado con el ID: " + id);
        }
    }

    @Override
    public boolean updateTask(Task task) {
        Task existingTask = this.findByIdTask(task.getId());
        if (existingTask == null) {
            throw new NoSuchElementException("Tarea no encontrada con el ID: " + task.getId());
        } else {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setCompleted(task.getCompleted());
            this.saveTask(existingTask);
            return true;
        }
    }
}
