package com.taemoi.project.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taemoi.project.entidades.Grado;
import com.taemoi.project.entidades.TipoGrado;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Long> {

	Grado findByTipoGrado(TipoGrado tipoGrado);

}
