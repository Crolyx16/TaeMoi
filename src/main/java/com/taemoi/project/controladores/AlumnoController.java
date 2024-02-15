package com.taemoi.project.controladores;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taemoi.project.dtos.AlumnoDTO;
import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.entidades.Categoria;
import com.taemoi.project.entidades.Grado;
import com.taemoi.project.errores.alumno.AlumnoDuplicadoException;
import com.taemoi.project.errores.alumno.AlumnoNoEncontradoException;
import com.taemoi.project.errores.alumno.DatosAlumnoInvalidosException;
import com.taemoi.project.errores.alumno.FechaNacimientoInvalidaException;
import com.taemoi.project.errores.alumno.ListaAlumnosVaciaException;
import com.taemoi.project.repositorios.AlumnoRepository;
import com.taemoi.project.repositorios.GradoRepository;
import com.taemoi.project.servicios.AlumnoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AlumnoService alumnoService;

	@Autowired
	private AlumnoRepository alumnoRepository;

	@Autowired
	private GradoRepository gradoRepository;

	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public Page<AlumnoDTO> obtenerAlumnosDTO(Pageable pageable) {
	    logger.info("## AlumnoController :: mostrarAlumnos");
	    pageable = PageRequest.of(pageable.getPageNumber(), 5, pageable.getSort());
	    Page<Alumno> alumnos = alumnoRepository.findAll(pageable);
	    if (alumnos.isEmpty()) {
	        throw new ListaAlumnosVaciaException("No hay usuarios registrados en el sistema.");
	    }
	    return alumnos.map(AlumnoDTO::deAlumno);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<AlumnoDTO> obtenerAlumnoPorIdDTO(@PathVariable Long id) {
		logger.info("## AlumnoController :: mostrarAlumnosPorId");
		Optional<AlumnoDTO> alumno = alumnoService.obtenerAlumnoDTOPorId(id);
		return alumno.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseThrow(() -> new AlumnoNoEncontradoException("Alumno no encontrado con ID: " + id));
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<AlumnoDTO> crearAlumno(@Valid @RequestBody AlumnoDTO nuevoAlumnoDTO) {
		logger.info("## AlumnoController :: añadirAlumno");

		if (!alumnoService.fechaNacimientoValida(nuevoAlumnoDTO.getFechaNacimiento())) {
			throw new FechaNacimientoInvalidaException("La fecha de nacimiento es inválida.");
		}

		if (!alumnoService.datosAlumnoValidos(nuevoAlumnoDTO)) {
			throw new DatosAlumnoInvalidosException("Los datos del alumno a crear son inválidos.");
		}

		int edad = alumnoService.calcularEdad(nuevoAlumnoDTO.getFechaNacimiento());

		Categoria categoria = alumnoService.asignarCategoriaSegunEdad(edad);
		Grado grado = alumnoService.asignarGradoSegunEdad(nuevoAlumnoDTO);

		Grado gradoGuardado = gradoRepository.findByTipoGrado(grado.getTipoGrado());
		if (gradoGuardado == null) {
			gradoGuardado = gradoRepository.save(grado);
		}

		Optional<Alumno> alumnoExistente = alumnoRepository.findByNif(nuevoAlumnoDTO.getNif());
		if (alumnoExistente.isPresent()) {
			throw new AlumnoDuplicadoException("El alumno con NIF " + nuevoAlumnoDTO.getNif() + " ya existe.");
		}

		Alumno nuevoAlumno = new Alumno();
		nuevoAlumno.setNombre(nuevoAlumnoDTO.getNombre());
		nuevoAlumno.setApellidos(nuevoAlumnoDTO.getApellidos());
		nuevoAlumno.setFechaNacimiento(nuevoAlumnoDTO.getFechaNacimiento());
		nuevoAlumno.setNumeroExpediente(nuevoAlumnoDTO.getNumeroExpediente());
		nuevoAlumno.setNif(nuevoAlumnoDTO.getNif());
		nuevoAlumno.setDireccion(nuevoAlumnoDTO.getDireccion());
		nuevoAlumno.setEmail(nuevoAlumnoDTO.getEmail());
		nuevoAlumno.setTelefono(nuevoAlumnoDTO.getTelefono());
		nuevoAlumno.setTipoTarifa(nuevoAlumnoDTO.getTipoTarifa());
		nuevoAlumno.setCuantiaTarifa(alumnoService.asignarCuantiaTarifa(nuevoAlumno.getTipoTarifa()));
		nuevoAlumno.setFechaAlta(nuevoAlumnoDTO.getFechaAlta());
		nuevoAlumno.setFechaBaja(nuevoAlumnoDTO.getFechaBaja());
		nuevoAlumno.setCategoria(categoria);
		nuevoAlumno.setGrado(grado);

		Alumno creado = alumnoService.crearAlumno(nuevoAlumno);

		AlumnoDTO creadoDTO = AlumnoDTO.deAlumno(creado);
		return new ResponseEntity<>(creadoDTO, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<AlumnoDTO> actualizarAlumno(@PathVariable Long id,
			@Valid @RequestBody AlumnoDTO alumnoActualizado) {
		logger.info("## AlumnoController :: modificarAlumno");

		if (!alumnoService.fechaNacimientoValida(alumnoActualizado.getFechaNacimiento())) {
			throw new FechaNacimientoInvalidaException("La fecha de nacimiento es inválida.");
		}

		if (!alumnoService.datosAlumnoValidos(alumnoActualizado)) {
			throw new DatosAlumnoInvalidosException("Los datos del alumno actualizado son inválidos.");
		}

		Date nuevaFechaNacimiento = alumnoActualizado.getFechaNacimiento();
		Alumno alumno = alumnoService.actualizarAlumno(id, alumnoActualizado, nuevaFechaNacimiento);
		AlumnoDTO alumnoActualizadoDTO = AlumnoDTO.deAlumno(alumno);
		return new ResponseEntity<>(alumnoActualizadoDTO, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> eliminarAlumno(@Valid @PathVariable Long id) {
		logger.info("## AlumnoController :: eliminarAlumno");
		boolean eliminado = alumnoService.eliminarAlumno(id);
		return eliminado ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}