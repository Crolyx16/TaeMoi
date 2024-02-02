package com.taemoi.project.configuracion;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.taemoi.project.entidades.Alumno;
import com.taemoi.project.entidades.TipoTarifa;
import com.taemoi.project.repositorios.AlumnoRepository;

@Profile("demo")
@Component
public class InicializadorDatos implements CommandLineRunner {
    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public void run(String... args) throws Exception {
        boolean borrarAlumnos = true;
        if (borrarAlumnos) {
            alumnoRepository.deleteAll();
        }

        Faker faker = new Faker(new Locale("es"));
        for (int i = 0; i < 10; i++) {
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

            alumnoRepository.save(alumno);
        }
    }
}
