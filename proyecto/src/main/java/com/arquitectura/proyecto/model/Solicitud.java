package com.arquitectura.proyecto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Solicitud.java
@Entity
@Data
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_profesor")
    private Profesor profesor;

    @ManyToOne
    @JoinColumn(name = "id_asignatura")
    private Asignatura asignatura;

    @ManyToOne
    @JoinColumn(name = "id_laboratorio")
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(name = "id_personal")
    private PersonalABS personal;

    private LocalDate fechaSolicitud;
    private LocalDate fechaUso;
    private LocalTime horario;
    private Integer cantGrupos;
    private Boolean estadoTarea;
    private String estadoAlerta;

    @OneToMany(mappedBy = "solicitud")
    private List<SolicitudInsumo> insumos;
}
