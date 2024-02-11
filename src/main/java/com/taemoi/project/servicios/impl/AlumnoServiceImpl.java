package com.taemoi.project.servicios.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taemoi.project.dtos.AlumnoDTO;
import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.entidades.Categoria;
import com.taemoi.project.entidades.Grado;
import com.taemoi.project.entidades.TipoCategoria;
import com.taemoi.project.entidades.TipoGrado;
import com.taemoi.project.entidades.TipoTarifa;
import com.taemoi.project.repositorios.AlumnoRepository;
import com.taemoi.project.repositorios.CategoriaRepository;
import com.taemoi.project.repositorios.GradoRepository;
import com.taemoi.project.servicios.AlumnoService;

@Service
public class AlumnoServiceImpl implements AlumnoService {

	private final AlumnoRepository alumnoRepository;

	public AlumnoServiceImpl(AlumnoRepository alumnoRepository) {
		this.alumnoRepository = alumnoRepository;
	}
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private GradoRepository gradoRepository;

	@Override
	public List<Alumno> obtenerTodosLosAlumnos() {
		return alumnoRepository.findAll();
	}

	@Override
	public Optional<Alumno> obtenerAlumnoPorId(Long id) {
		return alumnoRepository.findById(id);
	}

	@Override
	public Optional<AlumnoDTO> obtenerAlumnoDTOPorId(Long id) {
		Optional<Alumno> optionalAlumno = obtenerAlumnoPorId(id);
		return optionalAlumno.map(this::mapeoParaAlumnoDTO);
	}

	private AlumnoDTO mapeoParaAlumnoDTO(Alumno alumno) {
		if (alumno == null) {
			return null;
		}

		String categoriaNombre = alumno.getCategoria() != null ? alumno.getCategoria().getNombre() : null;
		String gradoTipo = alumno.getGrado() != null && alumno.getGrado().getTipoGrado() != null
				? alumno.getGrado().getTipoGrado().name()
				: null;

		return new AlumnoDTO(alumno.getNombre(), alumno.getApellidos(), alumno.getFechaNacimiento(),
				alumno.getNumeroExpediente(), alumno.getNif(), alumno.getDireccion(), alumno.getEmail(),
				alumno.getTelefono(), alumno.getCuantiaTarifa(), alumno.getTipoTarifa().name(), alumno.getFechaAlta(),
				alumno.getFechaBaja(), categoriaNombre, gradoTipo);
	}

	@Override
	public Alumno crearAlumno(Alumno alumno) {
		return alumnoRepository.save(alumno);
	}

	@Override
    public Alumno actualizarAlumno(Long id, Alumno alumnoActualizado) {
        Optional<Alumno> optionalAlumno = alumnoRepository.findById(id);
        if (optionalAlumno.isPresent()) {
            Alumno alumnoExistente = optionalAlumno.get();
            alumnoExistente.setNombre(alumnoActualizado.getNombre());
            alumnoExistente.setApellidos(alumnoActualizado.getApellidos());
            alumnoExistente.setFechaNacimiento(alumnoActualizado.getFechaNacimiento());
            alumnoExistente.setNumeroExpediente(alumnoActualizado.getNumeroExpediente());
            alumnoExistente.setNif(alumnoActualizado.getNif());
            alumnoExistente.setDireccion(alumnoActualizado.getDireccion());
            alumnoExistente.setEmail(alumnoActualizado.getEmail());
            alumnoExistente.setTelefono(alumnoActualizado.getTelefono());
            alumnoExistente.setTipoTarifa(alumnoActualizado.getTipoTarifa());
            alumnoExistente.setCuantiaTarifa(alumnoActualizado.getCuantiaTarifa());
            alumnoExistente.setFechaAlta(alumnoActualizado.getFechaAlta());
            alumnoExistente.setFechaBaja(alumnoActualizado.getFechaBaja());
            return alumnoRepository.save(alumnoExistente);
        } else {
            throw new RuntimeException("No se encontró el alumno con ID: " + id);
        }
    }

	@Override
	public boolean eliminarAlumno(Long id) {
		return alumnoRepository.findById(id).map(alumno -> {
			alumnoRepository.delete(alumno);
			return true;
		}).orElse(false);
	}
	
	@Override
	public Categoria asignarCategoriaSegunEdad(AlumnoDTO alumnoDTO) {
		int edad = calcularEdad(alumnoDTO.getFechaNacimiento());

		TipoCategoria tipoCategoria;
		if (edad >= 3 && edad <= 7) {
			tipoCategoria = TipoCategoria.PRETKD;
		} else if (edad >= 8 && edad <= 9) {
			tipoCategoria = TipoCategoria.INFANTIL;
		} else if (edad >= 10 && edad <= 11) {
			tipoCategoria = TipoCategoria.PRECADETE;
		} else if (edad >= 12 && edad <= 14) {
			tipoCategoria = TipoCategoria.CADETE;
		} else if (edad >= 15 && edad <= 17) {
			tipoCategoria = TipoCategoria.JUNIOR;
		} else if (edad >= 16 && edad <= 20) {
			tipoCategoria = TipoCategoria.SUB21;
		} else {
			tipoCategoria = TipoCategoria.SENIOR;
		}

		return categoriaRepository.findByNombre(tipoCategoria.getNombre());
	}

	@Override
	public Grado asignarGradoSegunEdad(AlumnoDTO nuevoAlumnoDTO) {
		LocalDate fechaNacimiento = nuevoAlumnoDTO.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
		boolean esMenorDeQuince = fechaNacimiento.isBefore(LocalDate.now().minusYears(15));

		List<TipoGrado> gradosDisponibles = esMenorDeQuince
				? Arrays.asList(TipoGrado.BLANCO, TipoGrado.BLANCO_AMARILLO, TipoGrado.AMARILLO,
						TipoGrado.AMARILLO_NARANJA, TipoGrado.NARANJA, TipoGrado.NARANJA_VERDE, TipoGrado.VERDE,
						TipoGrado.VERDE_AZUL, TipoGrado.AZUL, TipoGrado.AZUL_ROJO, TipoGrado.ROJO, TipoGrado.ROJO_NEGRO)
				: Arrays.asList(TipoGrado.BLANCO, TipoGrado.AMARILLO, TipoGrado.NARANJA, TipoGrado.VERDE,
						TipoGrado.AZUL, TipoGrado.ROJO, TipoGrado.NEGRO);

		TipoGrado tipoGradoAsignado = gradosDisponibles.get(new Random().nextInt(gradosDisponibles.size()));

		Grado gradoExistente = gradoRepository.findByTipoGrado(tipoGradoAsignado);

		if (gradoExistente != null) {
			return gradoExistente;
		}

		Grado grado = new Grado();
		grado.setTipoGrado(tipoGradoAsignado);
		return gradoRepository.save(grado);
	}

	@Override
	public int calcularEdad(Date fechaNacimiento) {
		LocalDate fechaNacimientoLocal = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate fechaActual = LocalDate.now();
		int edad = Period.between(fechaNacimientoLocal, fechaActual).getYears();

		return edad;
	}
}