package com.taemoi.project.servicios;

import java.util.List;
import java.util.Optional;

import com.taemoi.project.entidades.Alumno;

public interface AlumnoService {
	List<Alumno> obtenerTodosLosAlumnos();

	Optional<Alumno> obtenerAlumnoPorId(Long id);

	Alumno crearAlumno(Alumno alumno);

	Optional<Alumno> actualizarAlumno(Long id, Alumno alumnoActualizado);

	boolean eliminarAlumno(Long id);
}
