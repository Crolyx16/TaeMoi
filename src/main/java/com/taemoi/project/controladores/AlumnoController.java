package com.taemoi.project.controladores;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.servicios.AlumnoService;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;

	@GetMapping
	public List<Alumno> obtenerTodosLosAlumnos() {
		return alumnoService.obtenerTodosLosAlumnos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Alumno> obtenerAlumnoPorId(@PathVariable Long id) {
		Optional<Alumno> alumno = alumnoService.obtenerAlumnoPorId(id);
		return alumno.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<Alumno> crearAlumno(@RequestBody Alumno nuevoAlumno) {
		Alumno creado = alumnoService.crearAlumno(nuevoAlumno);
		return new ResponseEntity<>(creado, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Alumno> actualizarAlumno(@PathVariable Long id, @RequestBody Alumno alumnoActualizado) {
		Optional<Alumno> actualizado = alumnoService.actualizarAlumno(id, alumnoActualizado);
		return actualizado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarAlumno(@PathVariable Long id) {
		boolean eliminado = alumnoService.eliminarAlumno(id);
		return eliminado ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}