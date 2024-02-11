package com.taemoi.project.controladores;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taemoi.project.dtos.UsuarioDTO;
import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.servicios.AlumnoService;
import com.taemoi.project.servicios.UsuarioService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AlumnoService alumnoService;
	
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<Alumno> obtenerAlumnos() {
		logger.info("## AdminController :: mostrarAlumnos" );
		return alumnoService.obtenerTodosLosAlumnos();
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Alumno> obtenerAlumnoPorId(@PathVariable Long id) {
		logger.info("## AdminController :: mostrarAlumnosPorId" );
		Optional<Alumno> alumno = alumnoService.obtenerAlumnoPorId(id);
		return alumno.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> showUsers() {
    	logger.info("## AuthorizationAdminController :: showUsers" );
        List<UsuarioDTO> userList = usuarioService.obtenerTodos();
        return ResponseEntity.ok(userList);
    }
}
