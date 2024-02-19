package com.taemoi.project.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taemoi.project.dtos.AlumnoDTO;
import com.taemoi.project.entidades.Alumno;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    AlumnoDTO save(AlumnoDTO alumno);

    Optional<Alumno> findByNif(String nif);
    
	Page<Alumno> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

	List<Alumno> findByNombreContainingIgnoreCase(String nombre);
	
	Page<Alumno> findByCategoriaId(Long categoriaId, Pageable pageable);

	List<Alumno> findByCategoriaId(Long categoriaId);

	Page<Alumno> findByGradoId(Long gradoId, Pageable pageable);

	List<Alumno> findByGradoId(Long gradoId);

	Page<Alumno> findByNombreContainingIgnoreCaseAndGradoId(String nombre, Long gradoId, Pageable pageable);

	List<Alumno> findByNombreContainingIgnoreCaseAndGradoId(String nombre, Long gradoId);

	Page<Alumno> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Long categoriaId, Pageable pageable);
	
	List<Alumno> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Long categoriaId);

	Page<Alumno> findByGradoIdAndCategoriaId(Long gradoId, Long categoriaId, Pageable pageable);
	
	List<Alumno> findByGradoIdAndCategoriaId(Long gradoId, Long categoriaId);
	
	Page<Alumno> findByNombreContainingIgnoreCaseAndGradoIdAndCategoriaId(String nombre, Long gradoId, Long categoriaId,
			Pageable pageable);

	List<Alumno> findByNombreContainingIgnoreCaseAndGradoIdAndCategoriaId(String nombre, Long gradoId,
			Long categoriaId);
}