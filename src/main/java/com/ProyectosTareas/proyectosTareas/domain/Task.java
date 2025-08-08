package com.ProyectosTareas.proyectosTareas.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private Boolean completed;
    @Enumerated(EnumType.STRING)
    private StatusTask status;
    private Date due_date;
    private Timestamp created_at;
    private Timestamp updated_at;
    @ManyToOne
    @JsonBackReference
    private User user;
}

