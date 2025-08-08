package com.ProyectosTareas.proyectosTareas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ProyectosTareas.proyectosTareas.domain.ApiResponse;
import com.ProyectosTareas.proyectosTareas.domain.Task;
import com.ProyectosTareas.proyectosTareas.service.TaskInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class TaskController {

    private final TaskInterface taskinterface;

    @Autowired
    public TaskController(TaskInterface taskinterface) {
        this.taskinterface = taskinterface;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> handleRootTasks() {
        return new ResponseEntity<>(new ApiResponse("ER", 500, "Acceso a / no está permitido"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/error")
    @Operation(summary = "Operacion en caso de error en una tarea")
    public ResponseEntity<ApiResponse> error() {
        try {
            return new ResponseEntity<>(new ApiResponse("OK", 200, "Welcome"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("ER", 400, "Datos inválidos"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/task")
    @Operation(summary = "Lista las tareas existentes")
    @ApiResponses()
    public ResponseEntity<?> getAllTasks() {
        try {
            List<Task> tasks = taskinterface.listTask();
            if (tasks.isEmpty()) {
                log.info("Task is empty: {}", tasks);
                return new ResponseEntity<>(new ApiResponse("ER", 404, "La lista de tareas está vacía"), HttpStatus.NOT_FOUND);
            } else {
                log.info("Task: {}", tasks);
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error al obtener la lista de tareas", e);
            return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al obtener la lista de tareas"+e ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/task")
    @Operation(summary = "Guarda una tarea", description = "Elimina una tarea existente por su ID")
    @Parameter(name = "id",description = "ID de la tarea",required = true,schema = @Schema(type = "integer", example = "1"))
    @Parameter(name = "title",description = "title de la tarea",required = true,schema = @Schema(type = "integer", example = "1")    )
    @Parameter(name = "description",description = "description de la tarea",required = true,schema = @Schema(type = "integer", example = "1")    )
    public ResponseEntity<ApiResponse> saveTask(@RequestBody Task task, Errors err) {
        log.info("JSON recibido: {}", task);
        if (err.hasErrors()) {
            return new ResponseEntity<>(new ApiResponse("ER", 400, "Datos inválidos"), HttpStatus.BAD_REQUEST);
        }
        try {
            log.info("Guardando Tarea: {}", task);
            taskinterface.saveTask(task);
            return new ResponseEntity<>(new ApiResponse("OK", 200, "detalle Task correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al guardar el Task"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable("id") int id, @RequestBody Task task) {
        log.info("Datos recibidos: {}", task);
        try {
            Task existingTask = taskinterface.findByIdTask(id);
            if (existingTask == null) {
                return new ResponseEntity<>(new ApiResponse("ER", 404, "Task no encontrado"), HttpStatus.NOT_FOUND);
            }

            log.info("Tarea encontrada: {}", existingTask);

            // Solo actualizamos los campos que llegaron con cambios
            if (task.getTitle() != null) existingTask.setTitle(task.getTitle());
            if (task.getDescription() != null) existingTask.setDescription(task.getDescription());
            if (task.getStatus() != null) existingTask.setStatus(task.getStatus());
            existingTask.setCompleted(task.getCompleted());
            existingTask.setStatus(task.getStatus());
            existingTask.setDue_date(task.getDue_date());

            boolean resp = taskinterface.updateTask(existingTask);

            if (resp) {
                log.info("Tarea actualizada: {}", existingTask);
                return new ResponseEntity<>(new ApiResponse("OK", 0, "Task actualizado exitosamente"), HttpStatus.OK);
            } else {
                log.error("Error al actualizar la tarea: {}", existingTask);
                return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al actualizar la tarea"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error en updateTask", e);
            return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al actualizar la tarea " + e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/task/{id}")
    @Operation(summary = "Elimina una tarea",
            description = "Elimina una tarea existente por su ID",
            responses = {}
    )
    @Parameter(
            name = "id",
            description = "ID de la tarea a eliminar",
            required = true,
            schema = @Schema(type = "integer", example = "1")
    )
    public ResponseEntity<ApiResponse> deletetask(@PathVariable("id") int id) {

        try {
            Task tasking = taskinterface.findByIdTask(id);
            if (tasking == null) {
                return new ResponseEntity<>(new ApiResponse("ER", 404, "Task no encontrado"), HttpStatus.NOT_FOUND);
            }
            taskinterface.deleteTask(tasking);
            return new ResponseEntity<>(new ApiResponse("OK", 200, "Task eliminado exitosamente"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al eliminar el producto"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
