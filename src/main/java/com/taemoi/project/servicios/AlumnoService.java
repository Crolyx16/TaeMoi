package com.taemoi.project.servicios;

import java.util.List;
import java.util.Optional;

import com.taemoi.project.entidades.Alumno;

import jakarta.validation.Valid;

public interface AlumnoService {
	List<Alumno> obtenerTodosLosAlumnos();

	Optional<Alumno> obtenerAlumnoPorId(Long id);

	Alumno crearAlumno(@Valid Alumno alumno);

	Optional<Alumno> actualizarAlumno(@Valid Long id, Alumno alumnoActualizado);

	boolean eliminarAlumno(@Valid Long id);
}
