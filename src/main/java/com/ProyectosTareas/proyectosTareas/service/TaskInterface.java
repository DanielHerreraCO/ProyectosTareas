package com.ProyectosTareas.proyectosTareas.service;

import com.ProyectosTareas.proyectosTareas.domain.Task;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public interface TaskInterface {

    public List<Task> listTask();

    public void saveTask(Task task);

    public void deleteTask(Task task);

    public Task findTask(Task task);

    public Task findByIdTask(int id);

    public boolean updateTask(Task task);
}
