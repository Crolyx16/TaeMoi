package com.taemoi.project.errores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.taemoi.project.errores.alumno.AlumnoDuplicadoException;
import com.taemoi.project.errores.alumno.AlumnoNoEncontradoException;
import com.taemoi.project.errores.alumno.DatosAlumnoInvalidosException;
import com.taemoi.project.errores.alumno.FechaNacimientoInvalidaException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlumnoNoEncontradoException.class)
    public ResponseEntity<String> handleAlumnoNotFoundException(AlumnoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    
    @ExceptionHandler(AlumnoDuplicadoException.class)
    public ResponseEntity<String> handleAlumnoDuplicadoException(AlumnoDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    
    @ExceptionHandler(FechaNacimientoInvalidaException.class)
    public ResponseEntity<String> handleFechaNacimientoInvalidaException(FechaNacimientoInvalidaException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    
    @ExceptionHandler(DatosAlumnoInvalidosException.class)
    public ResponseEntity<String> handleDatosAlumnoInvalidosException(DatosAlumnoInvalidosException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}