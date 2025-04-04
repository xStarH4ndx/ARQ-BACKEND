package com.arquitectura.proyecto.service;

import com.arquitectura.proyecto.dto.CrearSolicitudInput;
import com.arquitectura.proyecto.dto.InsumoCantidadInput;
import com.arquitectura.proyecto.dto.SolicitudInput;
import com.arquitectura.proyecto.model.*;
import com.arquitectura.proyecto.model.SolicitudInsumo;
import com.arquitectura.proyecto.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalABSService {

    private final SolicitudRepository solicitudRepository;
    private final ProfesorRepository profesorRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final PersonalABSRepository personalABSRepository;
    private final InsumoRepository insumoRepository;
    private final SolicitudInsumoRepository solicitudInsumoRepository;

    public Solicitud crearSolicitud(CrearSolicitudInput input) {
        try {
            // Buscar entidades relacionadas
            Profesor profesor = profesorRepository.findById(input.getIdProfesor())
                    .orElseThrow(() -> new RuntimeException("Profesor con ID " + input.getIdProfesor() + " no encontrado"));

            Asignatura asignatura = asignaturaRepository.findById(input.getIdAsignatura())
                    .orElseThrow(() -> new RuntimeException("Asignatura con ID " + input.getIdAsignatura() + " no encontrada"));

            Laboratorio laboratorio = laboratorioRepository.findById(input.getIdLaboratorio())
                    .orElseThrow(() -> new RuntimeException("Laboratorio con ID " + input.getIdLaboratorio() + " no encontrado"));

            PersonalABS personal = personalABSRepository.findById(input.getIdPersonal())
                    .orElseThrow(() -> new RuntimeException("Personal ABS con ID " + input.getIdPersonal() + " no encontrado"));

            // Crear solicitud
            Solicitud solicitud = new Solicitud();
            solicitud.setProfesor(profesor);
            solicitud.setAsignatura(asignatura);
            solicitud.setLaboratorio(laboratorio);
            solicitud.setPersonal(personal);
            solicitud.setFechaSolicitud(LocalDate.now());

            // Validar fechas y horario
            try {
                solicitud.setFechaUso(LocalDate.parse(input.getFechaUso()));
                solicitud.setHorario(LocalTime.parse(input.getHorario()));
            } catch (Exception e) {
                throw new RuntimeException("Formato de fecha o horario inválido. Usa YYYY-MM-DD y HH:mm");
            }

            solicitud.setCantGrupos(input.getCantGrupos());
            solicitud.setEstado(false);

            // Guardar la solicitud
            Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

            // Validar insumos
            if (input.getInsumos() == null || input.getInsumos().isEmpty()) {
                throw new RuntimeException("La solicitud debe contener al menos un insumo.");
            }

            for (InsumoCantidadInput insumoInput : input.getInsumos()) {
                if (insumoInput.getCantidad() == null || insumoInput.getCantidad() <= 0) {
                    throw new RuntimeException("Cantidad inválida para insumo con ID " + insumoInput.getIdInsumo());
                }

                Insumo insumo = insumoRepository.findById(insumoInput.getIdInsumo())
                        .orElseThrow(() -> new RuntimeException("Insumo con ID " + insumoInput.getIdInsumo() + " no encontrado"));

                SolicitudInsumo si = new SolicitudInsumo();
                si.setSolicitud(solicitudGuardada);
                si.setInsumo(insumo);
                si.setCantidad(insumoInput.getCantidad());

                solicitudInsumoRepository.save(si);
            }

            return solicitudGuardada;

        } catch (Exception e) {
            // Imprimir en consola para depuración (reemplazar con logger si tienes configurado)
            e.printStackTrace();
            throw new RuntimeException("Error al crear la solicitud: " + e.getMessage());
        }
    }
    public Solicitud confirmarSolicitud(Long idSolicitud) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado(true);
        return solicitudRepository.save(solicitud);
    }


    public Solicitud modificarSolicitud(Long idSolicitud, Solicitud datosActualizados) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setFechaUso(datosActualizados.getFechaUso());
        solicitud.setHorario(datosActualizados.getHorario());
        solicitud.setCantGrupos(datosActualizados.getCantGrupos());
        solicitud.setEstado(datosActualizados.getEstado());

        return solicitudRepository.save(solicitud); // 👈 este objeto ya tiene un ID
    }



    public void eliminarSolicitud(Long idSolicitud) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        List<SolicitudInsumo> insumos = solicitudInsumoRepository.findBySolicitudId(idSolicitud);
        solicitudInsumoRepository.deleteAll(insumos);

        solicitudRepository.delete(solicitud);
    }


    public void confirmarYActualizarSolicitud(Long idSolicitud) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado(true);
        solicitudRepository.save(solicitud);

        // Restar insumos del stock
        List<SolicitudInsumo> solicitudInsumos = solicitudInsumoRepository.findBySolicitudId(idSolicitud);

        for (SolicitudInsumo si : solicitudInsumos) {
            Insumo insumo = si.getInsumo();
            int nuevoStock = insumo.getStockDisponible() - si.getCantidad().intValue();

            if (nuevoStock < 0) {
                throw new RuntimeException("❌ Stock insuficiente para el insumo: " + insumo.getNombre());
            }

            insumo.setStockDisponible(nuevoStock);
            insumoRepository.save(insumo);
        }
    }

}