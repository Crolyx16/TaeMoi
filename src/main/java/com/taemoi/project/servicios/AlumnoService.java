package com.taemoi.project.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.taemoi.project.dtos.AlumnoDTO;
import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.entidades.Categoria;
import com.taemoi.project.entidades.Grado;
import com.taemoi.project.entidades.TipoTarifa;

import jakarta.validation.Valid;

public interface AlumnoService {
	List<Alumno> obtenerTodosLosAlumnos();

	Optional<Alumno> obtenerAlumnoPorId(Long id);

	Optional<AlumnoDTO> obtenerAlumnoDTOPorId(Long id);

	Alumno crearAlumno(@Valid Alumno alumno);

	Alumno actualizarAlumno(@Valid Long id, AlumnoDTO alumnoActualizado, Date nuevaFechaNacimiento);

	boolean eliminarAlumno(@Valid Long id);
	
	double asignarCuantiaTarifa(TipoTarifa tipoTarifa);
	
	Categoria asignarCategoriaSegunEdad(int edad);
	
	Grado asignarGradoSegunEdad(AlumnoDTO nuevoAlumnoDTO);
	
	int calcularEdad(Date fechaNacimiento);
}
