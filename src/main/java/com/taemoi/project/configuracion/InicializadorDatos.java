package com.taemoi.project.configuracion;

import java.time.LocalDate;
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
        // Eliminar alumnos existentes si es necesario
        boolean borrarAlumnos = true;
        if (borrarAlumnos) {
            alumnoRepository.deleteAll();
        }

        Faker faker = new Faker(new Locale("es"));

        // Generar y guardar una categoría ficticia
        Categoria categoria = new Categoria();
        categoria.setNombre(faker.lorem().word());
        categoria.setEdadMinima(faker.number().numberBetween(1, 18));
        categoria.setEdadMaxima(faker.number().numberBetween(19, 99));
        categoriaRepository.save(categoria);

        // Asignar grados y guardar alumnos
        for (int i = 0; i < 10; i++) {
            Alumno alumno = generarAlumno(faker, categoria);
            alumnoRepository.save(alumno);
        }
    }

    private Alumno generarAlumno(Faker faker, Categoria categoria) {
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
        alumno.setCategoria(categoria);

        // Asignar el grado según la lógica específica
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

    private Grado asignarGradoSegunEdad(Alumno alumno, Faker faker) {
        List<TipoGrado> grados = (esMenorDeQuinceAnios(alumno.getFechaNacimiento()))
                ? Arrays.asList(TipoGrado.BLANCO, TipoGrado.BLANCO_AMARILLO, TipoGrado.AMARILLO, TipoGrado.AMARILLO_NARANJA,
                        TipoGrado.NARANJA, TipoGrado.NARANJA_VERDE, TipoGrado.VERDE, TipoGrado.VERDE_AZUL, TipoGrado.AZUL,
                        TipoGrado.AZUL_ROJO, TipoGrado.ROJO, TipoGrado.ROJO_NEGRO)
                : Arrays.asList(TipoGrado.BLANCO, TipoGrado.AMARILLO, TipoGrado.NARANJA, TipoGrado.VERDE, TipoGrado.AZUL,
                        TipoGrado.ROJO, TipoGrado.NEGRO);

        TipoGrado tipoGradoAsignado = grados.get(faker.number().numberBetween(0, grados.size()));
        Grado grado = new Grado();
        grado.setTipoGrado(tipoGradoAsignado);

        return grado;
    }

    private boolean esMenorDeQuinceAnios(Date fechaNacimiento) {
        LocalDate fechaNacimientoLocal = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaLimite = LocalDate.now().minusYears(15);
        return fechaNacimientoLocal.isAfter(fechaLimite);
    }
}