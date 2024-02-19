package com.taemoi.project.servicios.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Autowired
	private AlumnoRepository alumnoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private GradoRepository gradoRepository;

	@Override
	public Page<Alumno> obtenerTodosLosAlumnos(Pageable pageable) {
		return alumnoRepository.findAll(pageable);
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
	public Optional<AlumnoDTO> obtenerAlumnoDTOPorId(Long id) {
		Optional<Alumno> optionalAlumno = obtenerAlumnoPorId(id);
		return optionalAlumno.map(this::mapeoParaAlumnoDTO);
	}
	
	@Override
    public Page<Alumno> obtenerAlumnosPorNombre(String nombre, Pageable pageable) {
        return alumnoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

	/*
	@Override
    public Page<Alumno> obtenerAlumnosPorCategoria(Long categoriaId, Pageable pageable) {
        return alumnoRepository.findByCategoriaId(categoriaId, pageable);
    }

	@Override
    public Page<Alumno> obtenerAlumnosPorGrado(Long gradoId, Pageable pageable) {
        return alumnoRepository.findByGradoId(gradoId, pageable);
    }
	
	@Override
	public Page<Alumno> obtenerAlumnosPorNombreCategoriaYGrado(String nombre, Long categoriaId, Long gradoId,
			Pageable pageable) {
		return alumnoRepository.findByNombreContainingIgnoreCaseAndCategoriaIdAndGradoId(nombre, categoriaId, gradoId, pageable);
	}

	@Override
	public Page<Alumno> obtenerAlumnosPorNombreYCategoria(String nombre, Long categoriaId, Pageable pageable) {
		return alumnoRepository.findByNombreContainingIgnoreCaseAndCategoriaId(nombre, categoriaId, pageable);
	}

	@Override
	public Page<Alumno> obtenerAlumnosPorNombreYGrado(String nombre, Long gradoId, Pageable pageable) {
		return alumnoRepository.findByNombreContainingIgnoreCaseAndGradoId(nombre, gradoId, pageable);
	}

	@Override
	public Page<Alumno> obtenerAlumnosPorCategoriaYGrado(Long categoriaId, Long gradoId, Pageable pageable) {
		return alumnoRepository.findByCategoriaIdAndGradoId(categoriaId, gradoId, pageable);
	}*/

	@Override
	public Alumno crearAlumno(Alumno alumno) {
		return alumnoRepository.save(alumno);
	}

	@Override
	public Alumno actualizarAlumno(Long id, AlumnoDTO alumnoActualizado, Date nuevaFechaNacimiento) {
		Optional<Alumno> optionalAlumno = alumnoRepository.findById(id);
		if (optionalAlumno.isPresent()) {
			Alumno alumnoExistente = optionalAlumno.get();
			alumnoExistente.setNombre(alumnoActualizado.getNombre());
			alumnoExistente.setApellidos(alumnoActualizado.getApellidos());
			alumnoExistente.setFechaNacimiento(nuevaFechaNacimiento);

			int nuevaEdad = calcularEdad(nuevaFechaNacimiento);

			Categoria nuevaCategoria = asignarCategoriaSegunEdad(nuevaEdad);
			alumnoExistente.setCategoria(nuevaCategoria);

			alumnoExistente.setNumeroExpediente(alumnoActualizado.getNumeroExpediente());
			alumnoExistente.setNif(alumnoActualizado.getNif());
			alumnoExistente.setDireccion(alumnoActualizado.getDireccion());
			alumnoExistente.setEmail(alumnoActualizado.getEmail());
			alumnoExistente.setTelefono(alumnoActualizado.getTelefono());
			alumnoExistente.setTipoTarifa(alumnoActualizado.getTipoTarifa());
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
	public double asignarCuantiaTarifa(TipoTarifa tipoTarifa) {
		switch (tipoTarifa) {
		case ADULTO:
			return 30.0;
		case ADULTO_GRUPO:
			return 20.0;
		case FAMILIAR:
			return 0.0;
		case INFANTIL:
			return 25.0;
		case INFANTIL_GRUPO:
			return 20.0;
		case HERMANOS:
			return 23.0;
		case PADRES_HIJOS:
			return 0.0;
		default:
			throw new IllegalArgumentException("Tipo de tarifa no válido: " + tipoTarifa);
		}
	}

	@Override
	public Categoria asignarCategoriaSegunEdad(int edad) {
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

	@Override
	public boolean fechaNacimientoValida(Date fechaNacimiento) {
		Calendar fechaActualMenos3Anios = Calendar.getInstance();
		fechaActualMenos3Anios.add(Calendar.YEAR, -3);

		Calendar fechaNacimientoCalendar = Calendar.getInstance();
		fechaNacimientoCalendar.setTime(fechaNacimiento);

		return fechaNacimientoCalendar.before(fechaActualMenos3Anios);
	}

	@Override
	public boolean datosAlumnoValidos(AlumnoDTO alumnoDTO) {
		if (alumnoDTO.getNombre() == null || alumnoDTO.getNombre().isEmpty() || alumnoDTO.getApellidos() == null
				|| alumnoDTO.getApellidos().isEmpty()) {
			return false;
		}
		if (alumnoDTO.getFechaNacimiento() == null || alumnoDTO.getFechaNacimiento().after(new Date())) {
			return false;
		}
		if (alumnoDTO.getNumeroExpediente() == null || alumnoDTO.getNumeroExpediente().isEmpty()
				|| alumnoDTO.getNif() == null || alumnoDTO.getNif().isEmpty()) {
			return false;
		}
		if (alumnoDTO.getDireccion() == null || alumnoDTO.getDireccion().isEmpty() || alumnoDTO.getEmail() == null
				|| alumnoDTO.getEmail().isEmpty()) {
			return false;
		}
		if (alumnoDTO.getTelefono() != null && alumnoDTO.getTelefono() <= 0) {
			return false;
		}
		if (alumnoDTO.getTipoTarifa() == null) {
			return false;
		}
		if (alumnoDTO.getFechaAlta() == null || alumnoDTO.getFechaAlta().after(new Date())) {
			return false;
		}
		return true;
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
				alumno.getTelefono(), alumno.getCuantiaTarifa(), alumno.getTipoTarifa(), alumno.getFechaAlta(),
				alumno.getFechaBaja(), categoriaNombre, gradoTipo);
	}
}