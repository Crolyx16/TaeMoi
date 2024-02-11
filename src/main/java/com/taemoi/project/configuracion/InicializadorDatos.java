package com.taemoi.project.configuracion;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.entidades.Categoria;
import com.taemoi.project.entidades.Grado;
import com.taemoi.project.entidades.TipoCategoria;
import com.taemoi.project.entidades.TipoGrado;
import com.taemoi.project.entidades.TipoTarifa;
import com.taemoi.project.repositorios.AlumnoRepository;
import com.taemoi.project.repositorios.CategoriaRepository;
import com.taemoi.project.repositorios.GradoRepository;

@Component
public class InicializadorDatos implements CommandLineRunner {

	@Autowired
	private AlumnoRepository alumnoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private GradoRepository gradoRepository;

	@Override
	public void run(String... args) throws Exception {
		boolean borrarAlumnos = true;
		if (borrarAlumnos) {
			alumnoRepository.deleteAll();
		}

		Faker faker = new Faker(new Locale("es"));

		// Crear y almacenar las categorías en la base de datos
		for (TipoCategoria tipoCategoria : TipoCategoria.values()) {
			Categoria categoriaExistente = categoriaRepository.findByNombre(tipoCategoria.getNombre());
			if (categoriaExistente == null) {
				Categoria categoria = new Categoria();
				categoria.setNombre(tipoCategoria.getNombre());
				categoriaRepository.save(categoria);
			}
		}

		// Asignar grados y guardar alumnos
		for (int i = 0; i < 10; i++) {
			Alumno alumno = generarAlumno(faker);
			alumnoRepository.save(alumno);
		}
	}

	private Alumno generarAlumno(Faker faker) {
		Alumno alumno = new Alumno();
		alumno.setNombre(faker.name().firstName());
		alumno.setApellidos(faker.name().lastName());
		alumno.setNumeroExpediente(faker.number().digits(8));
		alumno.setFechaNacimiento(faker.date().birthday());
		alumno.setNif(faker.idNumber().valid());
		alumno.setDireccion(faker.address().fullAddress());
		alumno.setTelefono(faker.number().numberBetween(100000000, 999999999));
		alumno.setEmail(faker.internet().emailAddress());
		alumno.setTipoTarifa(TipoTarifa.values()[faker.number().numberBetween(0, TipoTarifa.values().length)]);
		alumno.setCuantiaTarifa(faker.number().randomDouble(2, 50, 200));
		alumno.setFechaAlta(faker.date().birthday());
		alumno.setFechaBaja(faker.date().birthday());

		// Obtener la edad del alumno
		LocalDate fechaNacimiento = alumno.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
		int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

		// Asignar la categoría según la edad del alumno
		alumno.setCategoria(asignarCategoriaSegunEdad(edad));

		Grado grado = asignarGradoSegunEdad(alumno, faker);

		// Verificar si ya existe un grado con el mismo tipo en la base de datos
		Grado gradoExistente = gradoRepository.findByTipoGrado(grado.getTipoGrado());

		if (gradoExistente != null) {
			// Si existe, asignar el grado existente al alumno
			alumno.setGrado(gradoExistente);
		} else {
			gradoRepository.save(grado);
			alumno.setGrado(grado);
		}

		return alumno;
	}

	private Categoria asignarCategoriaSegunEdad(int edad) {
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

	private Grado asignarGradoSegunEdad(Alumno alumno, Faker faker) {
		List<TipoGrado> grados;
		if (esMenorDeQuinceAnios(alumno.getFechaNacimiento())) {
			grados = Arrays.asList(TipoGrado.BLANCO, TipoGrado.BLANCO_AMARILLO, TipoGrado.AMARILLO,
					TipoGrado.AMARILLO_NARANJA, TipoGrado.NARANJA, TipoGrado.NARANJA_VERDE, TipoGrado.VERDE,
					TipoGrado.VERDE_AZUL, TipoGrado.AZUL, TipoGrado.AZUL_ROJO, TipoGrado.ROJO, TipoGrado.ROJO_NEGRO);
		} else {
			grados = Arrays.asList(TipoGrado.BLANCO, TipoGrado.AMARILLO, TipoGrado.NARANJA, TipoGrado.VERDE,
					TipoGrado.AZUL, TipoGrado.ROJO, TipoGrado.NEGRO);
		}

		TipoGrado tipoGradoAsignado = grados.get(faker.number().numberBetween(0, grados.size()));
		Grado grado = new Grado();
		grado.setTipoGrado(tipoGradoAsignado);

		return grado;
	}

	private boolean esMenorDeQuinceAnios(Date fechaNacimiento) {
		LocalDate fechaNacimientoLocal = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate fechaLimite = LocalDate.now().minusYears(15);
		return fechaNacimientoLocal.isBefore(fechaLimite); // Cambiado de "isAfter" a "isBefore"
	}
}