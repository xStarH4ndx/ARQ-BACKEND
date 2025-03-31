package com.arquitectura.arquitectura.service;

import com.arquitectura.arquitectura.model.Asignatura;
import com.arquitectura.arquitectura.repository.AsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    public List<Asignatura> obtenerTodasAsignaturas() {
        return asignaturaRepository.findAll();
    }
}
