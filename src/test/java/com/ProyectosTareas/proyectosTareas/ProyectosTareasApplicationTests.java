package com.ProyectosTareas.proyectosTareas;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ProyectosTareas.proyectosTareas.domain.Task;
import com.ProyectosTareas.proyectosTareas.domain.User;
import com.ProyectosTareas.proyectosTareas.service.TaskInterface;
import com.ProyectosTareas.proyectosTareas.service.UserInterface;

@SpringBootTest
@ActiveProfiles("test")
class ProyectosTareasApplicationTests {

	@Autowired
	private final UserInterface userInterface;

	@Autowired
	private final TaskInterface taskInterface;

	ProyectosTareasApplicationTests(UserInterface userInterface, TaskInterface taskInterface) {
		this.userInterface = userInterface;
		this.taskInterface = taskInterface;
	}


	@BeforeEach
	void limpiarDatos() {
		// Eliminar todos los usuarios y tareas antes de cada test
		userInterface.listUser().forEach(userInterface::deleteUser);
		taskInterface.listTask().forEach(taskInterface::deleteTask);
	}

	//@Test
	void crearUsuario() {
		User user = new User();
		user.setUsername("Juan");
		user.setEmail("juan@example.com");
		user.setPassword("123456");

		userInterface.saveUser(user);
		List<User> users = userInterface.listUser();
		assertThat(users.size()).isEqualTo(1);
		User saved = users.get(0);
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getUsername()).isEqualTo("Juan");
		assertThat(saved.getEmail()).isEqualTo("juan@example.com");
	}


	//@Test
	void crearTarea() {
		Task task = new Task();
		task.setTitle("Crear base de datos");
		task.setDescription("Diseñar y crear el esquema inicial de la base de datos");

		taskInterface.saveTask(task);
		List<Task> tasks = taskInterface.listTask();
		assertThat(tasks.size()).isEqualTo(1);
		Task saved = tasks.get(0);
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getTitle()).isEqualTo("Crear base de datos");
		assertThat(saved.getDescription()).isEqualTo("Diseñar y crear el esquema inicial de la base de datos");
	}


	//@Test
	void listarTareas() {
		Task t1 = new Task();
		t1.setTitle("Tarea 1");
		Task t2 = new Task();
		t2.setTitle("Tarea 2");

		taskInterface.saveTask(t1);
		taskInterface.saveTask(t2);

		List<Task> tareas = taskInterface.listTask();
		assertThat(tareas.size()).isEqualTo(2);
		assertThat(tareas.get(0).getTitle()).isEqualTo("Tarea 1");
		assertThat(tareas.get(1).getTitle()).isEqualTo("Tarea 2");
	}

	//@Test
	void actualizarTarea() {
		Task task = new Task();
		task.setTitle("Original");
		task.setDescription("Desc");
		taskInterface.saveTask(task);
		List<Task> tasks = taskInterface.listTask();
		Task saved = tasks.get(0);
		saved.setTitle("Modificado");
		boolean updated = taskInterface.updateTask(saved);
		assertThat(updated).isTrue();
		Task modificado = taskInterface.findByIdTask(saved.getId());
		assertThat(modificado.getTitle()).isEqualTo("Modificado");
	}

	//@Test
	void eliminarTarea() {
		Task task = new Task();
		task.setTitle("Eliminar");
		taskInterface.saveTask(task);
		List<Task> tasks = taskInterface.listTask();
		assertThat(tasks.size()).isEqualTo(1);
		taskInterface.deleteTask(tasks.get(0));
		List<Task> after = taskInterface.listTask();
		assertThat(after.size()).isEqualTo(0);
	}

	//@Test
	void actualizarUsuario() {
		User user = new User();
		user.setUsername("Pedro");
		user.setEmail("pedro@example.com");
		user.setPassword("abc123");
		userInterface.saveUser(user);
		List<User> users = userInterface.listUser();
		User saved = users.get(0);
		saved.setUsername("PedroMod");
		boolean updated = userInterface.updateUser(saved);
		assertThat(updated).isTrue();
		User modificado = userInterface.findByIdUser(saved.getId());
		assertThat(modificado.getUsername()).isEqualTo("PedroMod");
	}

	//@Test
	void eliminarUsuario() {
		User user = new User();
		user.setUsername("EliminarU");
		user.setEmail("eliminar@example.com");
		user.setPassword("pass");
		userInterface.saveUser(user);
		List<User> users = userInterface.listUser();
		assertThat(users.size()).isEqualTo(1);
		userInterface.deleteUser(users.get(0));
		List<User> after = userInterface.listUser();
		assertThat(after.size()).isEqualTo(0);
	}
	//@Test
	void contextLoads() {
	}

}
