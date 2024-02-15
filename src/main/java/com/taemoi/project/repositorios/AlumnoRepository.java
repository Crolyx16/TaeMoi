package com.taemoi.project.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taemoi.project.dtos.AlumnoDTO;
import com.taemoi.project.entidades.Alumno;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

	AlumnoDTO save(AlumnoDTO alumno);

	Optional<Alumno> findByNif(String nif);
}