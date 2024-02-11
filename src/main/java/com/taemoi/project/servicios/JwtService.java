package com.taemoi.project.servicios;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extraerNombreUsuario(String token);
    String generarToken(UserDetails userDetails);
    boolean comprobarToken(String token, UserDetails userDetails);
}
