package com.arquitectura.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    @OneToMany(mappedBy = "profesor")
    private List<Solicitud> solicitudes;
}
