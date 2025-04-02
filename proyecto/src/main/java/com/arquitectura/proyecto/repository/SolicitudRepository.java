package com.arquitectura.proyecto.repository;

import com.arquitectura.proyecto.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findAllByOrderByFechaUsoAsc();

    List<Solicitud> findAllByOrderByAsignaturaNombreAsc();
}
