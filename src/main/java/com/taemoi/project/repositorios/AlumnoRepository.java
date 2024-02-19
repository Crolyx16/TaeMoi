package com.taemoi.project.repositorios;

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

    /*
    Page<Alumno> findByCategoriaId(Long categoriaId, Pageable pageable);

    Page<Alumno> findByGradoId(Long gradoId, Pageable pageable);

    Page<Alumno> findByNombreContainingIgnoreCaseAndCategoriaIdAndGradoId(String nombre, Long categoriaId, Long gradoId, Pageable pageable);

    Page<Alumno> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Long categoriaId, Pageable pageable);

    Page<Alumno> findByNombreContainingIgnoreCaseAndGradoId(String nombre, Long gradoId, Pageable pageable);

    Page<Alumno> findByCategoriaIdAndGradoId(Long categoriaId, Long gradoId, Pageable pageable);
    */
}