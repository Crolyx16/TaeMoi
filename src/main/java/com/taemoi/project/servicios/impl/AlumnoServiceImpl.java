package com.taemoi.project.servicios.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.repositorios.AlumnoRepository;
import com.taemoi.project.servicios.AlumnoService;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public AlumnoServiceImpl(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

	@Override
    public List<Alumno> obtenerTodosLosAlumnos() {
        return alumnoRepository.findAll();
    }
	
	@Override
	public Optional<Alumno> obtenerAlumnoPorId(Long id) {
		return alumnoRepository.findById(id);
	}

	@Override
	public Alumno crearAlumno(Alumno alumno) {
		return alumnoRepository.save(alumno);
	}

	@Override
	public Optional<Alumno> actualizarAlumno(Long id, Alumno alumnoActualizado) {
		return alumnoRepository.findById(id).map(alumno -> {
			alumno.setNombre(alumnoActualizado.getNombre());
			alumno.setApellidos(alumnoActualizado.getApellidos());
			return alumnoRepository.save(alumno);
		});
	}

	@Override
	public boolean eliminarAlumno(Long id) {
		return alumnoRepository.findById(id).map(alumno -> {
			alumnoRepository.delete(alumno);
			return true;
		}).orElse(false);
	}
}