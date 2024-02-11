package com.taemoi.project.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.taemoi.project.dtos.AlumnoDTO;
import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.entidades.Categoria;
import com.taemoi.project.entidades.Grado;

import jakarta.validation.Valid;

public interface AlumnoService {
	List<Alumno> obtenerTodosLosAlumnos();

	Optional<Alumno> obtenerAlumnoPorId(Long id);

	Optional<AlumnoDTO> obtenerAlumnoDTOPorId(Long id);

	Alumno crearAlumno(@Valid Alumno alumno);

	Alumno actualizarAlumno(@Valid Long id, Alumno alumnoActualizado);

	boolean eliminarAlumno(@Valid Long id);
	
	Categoria asignarCategoriaSegunEdad(AlumnoDTO alumnoDTO);
	
	Grado asignarGradoSegunEdad(AlumnoDTO nuevoAlumnoDTO);
	
	int calcularEdad(Date fechaNacimiento);
}
