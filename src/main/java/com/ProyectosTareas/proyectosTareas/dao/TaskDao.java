package com.ProyectosTareas.proyectosTareas.dao;

import com.ProyectosTareas.proyectosTareas.domain.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDao extends CrudRepository<Task, Long> {
}
