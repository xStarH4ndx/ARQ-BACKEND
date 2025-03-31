package com.arquitectura.arquitectura.resolver;

import com.arquitectura.arquitectura.model.Asignatura;
import com.arquitectura.arquitectura.service.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsignaturaResolver {

    @Autowired
    private AsignaturaService asignaturaService;

    @QueryMapping
    public List<Asignatura> obtenerTodasAsignaturas() {
        return asignaturaService.obtenerTodasAsignaturas();
    }
}
