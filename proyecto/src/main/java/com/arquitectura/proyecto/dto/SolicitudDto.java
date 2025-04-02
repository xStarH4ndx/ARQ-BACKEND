package com.arquitectura.proyecto.dto;

import lombok.Data;

import java.util.List;

@Data
public class SolicitudDto {
    private Long id;
    private String fechaSolicitud;
    private String fechaUso;
    private String horario;
    private Integer cantGrupos;
    private Boolean estadoTarea;
    private String estadoAlerta;

    private ProfesorDto profesor;
    private AsignaturaDto asignatura;
    private LaboratorioDto laboratorio;
    private PersonalABSDto personalABS;
    private List<SolicitudInsumoDto> insumos;
}
