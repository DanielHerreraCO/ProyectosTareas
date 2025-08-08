package com.ProyectosTareas.proyectosTareas.controller;

import com.ProyectosTareas.proyectosTareas.domain.ApiResponse;
import com.ProyectosTareas.proyectosTareas.domain.Task;
import com.ProyectosTareas.proyectosTareas.domain.User;
import com.ProyectosTareas.proyectosTareas.domain.LoginRequest;
import com.ProyectosTareas.proyectosTareas.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    private final UserInterface userInterface;
    private final UserRepository userRepository;

    public UserController(UserInterface userInterface, UserRepository userRepository) {
        this.userInterface = userInterface;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> handleRootUser() {
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

    @GetMapping("/users")
    @Operation(summary = "Listar users existentes")
    @ApiResponses()
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userInterface.listUser();
            if (users.isEmpty()) {
                log.info("users is empty: {}", users);
                return new ResponseEntity<>(new ApiResponse("ER", 404, "La lista de users está vacía"), HttpStatus.NOT_FOUND);
            } else {
                log.info("users: {}", users);
                return new ResponseEntity<List<User>>(users, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error al obtener la lista de users", e);
            return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al obtener la lista de users"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    @Operation(summary = "Guarda un users", description = "guarda un users")
    public ResponseEntity<ApiResponse> saveUser(@Valid @RequestBody User users, Errors err) {
        log.info("JSON recibido: {}", users);
        if (err.hasErrors()) {
            return new ResponseEntity<>(new ApiResponse("ER", 400, "Datos inválidos"), HttpStatus.BAD_REQUEST);
        }
        try {
            log.info("Guardando users: {}", users);
            userInterface.saveUser(users);
            return new ResponseEntity<>(new ApiResponse("OK", 200, "users guardado correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al guardar el users"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Actualizar un users")
    @ApiResponses()
    @Parameter(
            name = "id",
            description = "ID de la tarea a Actualizar",
            required = true,
            schema = @Schema(type = "integer", example = "1")
    )
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("id") BigInteger id, @RequestBody User users) {
        log.info("Datos recibidos: {}", users);
        try {

            User existingUser = userInterface.findByIdUser(id);
            if (existingUser == null) {
                return new ResponseEntity<>(new ApiResponse("ER", 404, "users no encontrado"), HttpStatus.NOT_FOUND);
            } else {
                log.info("users encontrados: {}", existingUser);
                boolean resp = userInterface.updateUser(users);
                if (resp) {
                    log.info("users actualizado: {}", existingUser);
                    return new ResponseEntity<>(new ApiResponse("OK", 0, "users actualizado exitosamente"), HttpStatus.OK);
                } else {
                    log.info("Error al actualizar users: {}", existingUser);
                    return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al actualizar users"), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al actualizar users " + e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Elimina un users",
            description = "Elimina un users existente por su ID",
            responses = {}
    )
    @Parameter(
            name = "id",
            description = "ID de users a eliminar",
            required = true,
            schema = @Schema(type = "integer", example = "1")
    )
    public ResponseEntity<ApiResponse> deleteusers(@PathVariable("id") BigInteger id) {

        try {
            User users = userInterface.findByIdUser(id);
            if (users == null) {
                return new ResponseEntity<>(new ApiResponse("ER", 404, "users no encontrado"), HttpStatus.NOT_FOUND);
            }
            userInterface.deleteUser(users);
            return new ResponseEntity<>(new ApiResponse("OK", 200, "users eliminado exitosamente"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("ER", 500, "Error al eliminar el users"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> user = userRepository.findByEmailAndPassword(request.getEmail(), request.getpassword());

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }
}
