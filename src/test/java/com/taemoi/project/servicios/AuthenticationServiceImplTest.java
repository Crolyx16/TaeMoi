package com.taemoi.project.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.taemoi.project.dtos.request.LoginRequest;
import com.taemoi.project.dtos.request.RegistroRequest;
import com.taemoi.project.dtos.response.JwtAuthenticationResponse;
import com.taemoi.project.entidades.Usuario;
import com.taemoi.project.repositorios.UsuarioRepository;
import com.taemoi.project.servicios.impl.AuthenticationServiceImpl;

@SuppressWarnings("deprecation")
public class AuthenticationServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    {
        MockitoAnnotations.initMocks(this); // Inicializa las anotaciones de Mockito
    }
    
    @Test
    public void testSignup() {
        // Configurar datos de prueba
        RegistroRequest registroRequest = new RegistroRequest("nombre", "apellidos", "correo@example.com", "contraseña");

        // Mockear el comportamiento del repositorio de usuarios
        when(usuarioRepository.existsByEmail(registroRequest.getEmail())).thenReturn(false);

        // Mockear el comportamiento del codificador de contraseñas
        when(passwordEncoder.encode(registroRequest.getContrasena())).thenReturn("contraseñaCodificada");

        // Mockear el comportamiento del servicio JWT
        when(jwtService.generateToken(any())).thenReturn("tokenJWT");

        // Ejecutar el método de registro de usuarios
        JwtAuthenticationResponse respuesta = authenticationService.signup(registroRequest);

        // Verificar el resultado
        assertNotNull(respuesta);
        assertEquals("tokenJWT", respuesta.getToken());
    }
    
    @Test
    public void testSignin() {
        // Configurar datos de prueba
        LoginRequest loginRequest = new LoginRequest("correo@example.com", "contraseña");

        // Mockear el comportamiento del repositorio de usuarios
        Usuario usuario = new Usuario();
        usuario.setNombre("nombre");
        usuario.setApellidos("apellidos");
        usuario.setEmail("correo@example.com");
        usuario.setContrasena("contraseñaCodificada");
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(usuario));

        // Mockear el comportamiento del AuthenticationManager
        // (No es necesario para esta prueba porque no estamos probando la autenticación real)

        // Mockear el comportamiento del servicio JWT
        when(jwtService.generateToken(any())).thenReturn("tokenJWT");

        // Ejecutar el método de inicio de sesión de usuarios
        JwtAuthenticationResponse respuesta = authenticationService.signin(loginRequest);

        // Verificar el resultado
        assertNotNull(respuesta);
        assertEquals("tokenJWT", respuesta.getToken());
    }
}