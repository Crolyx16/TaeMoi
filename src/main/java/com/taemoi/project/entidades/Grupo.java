package com.taemoi.project.entidades;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Grupo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "alumno_id")
	private Alumno alumno;

	@Column(name = "numero_expediente_alumno")
	private String numeroExpedienteAlumno;

	@Column(name = "dia_semana")
	private int diaSemana;

	private String dia;

	private String turno;

	@Column(name = "hora_inicio")
	private String horaInicio;

	@Column(name = "hora_fin")
	private String horaFin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public String getNumeroExpedienteAlumno() {
		return numeroExpedienteAlumno;
	}

	public void setNumeroExpedienteAlumno(String numeroExpedienteAlumno) {
		this.numeroExpedienteAlumno = numeroExpedienteAlumno;
	}

	public int getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(int diaSemana) {
		this.diaSemana = diaSemana;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
		actualizarHorasPorTurno(turno);
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	private void actualizarHorasPorTurno(String turno) {
		// Verificar si el formato del turno es correcto
		if (turno != null && turno.matches("TURNO DE \\d{2}:\\d{2} A \\d{2}:\\d{2}")) {
			// Extraer las horas de inicio y fin del turno
			String[] partes = turno.split(" ");
			String horaInicioFin = partes[3]; // "hh:mm A hh:mm"
			String[] horas = horaInicioFin.split(" A ");

			// Actualizar las horas en la entidad Grupo
			this.horaInicio = horas[0];
			this.horaFin = horas[1];
		} else {
			// Manejar el caso en que el formato del turno no sea correcto
			throw new IllegalArgumentException("Formato de turno no válido: " + turno);
		}
	}

	// Equals y hashCode para comparar objetos Grupo en caso de querer mapearlos
	// fácilmente

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Grupo grupo = (Grupo) o;
		return Objects.equals(alumno, grupo.alumno) && diaSemana == grupo.diaSemana
				&& Objects.equals(turno, grupo.turno);
	}

	@Override
	public int hashCode() {
		return Objects.hash(alumno, diaSemana, turno);
	}

}