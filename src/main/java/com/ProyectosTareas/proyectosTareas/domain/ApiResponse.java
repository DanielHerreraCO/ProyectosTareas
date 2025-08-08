package com.ProyectosTareas.proyectosTareas.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa una respuesta genérica de la API")
public class ApiResponse {
    @Schema(description = "Código de estado personalizado", example = "ER")
    private String status;
    @Schema(description = "Código HTTP", example = "404")
    private int errorCode;
    @Schema(description = "Mensaje descriptivo", example = "La lista de tareas está vacía")
    private String message;

    public ApiResponse(String status, int errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    // Getters y Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}