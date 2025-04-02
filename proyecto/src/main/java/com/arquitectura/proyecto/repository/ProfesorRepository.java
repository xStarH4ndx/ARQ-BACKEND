package com.arquitectura.proyecto.repository;

import com.arquitectura.proyecto.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}
