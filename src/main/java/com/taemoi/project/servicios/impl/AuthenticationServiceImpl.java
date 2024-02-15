package com.taemoi.project.servicios.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taemoi.project.dtos.request.LoginRequest;
import com.taemoi.project.dtos.request.RegistroRequest;
import com.taemoi.project.dtos.response.JwtAuthenticationResponse;
import com.taemoi.project.entidades.Roles;
import com.taemoi.project.entidades.Usuario;
import com.taemoi.project.repositorios.UsuarioRepository;
import com.taemoi.project.servicios.AuthenticationService;
import com.taemoi.project.servicios.JwtService;

import lombok.Builder;

@Builder
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationServiceImpl(PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public JwtAuthenticationResponse signup(RegistroRequest request) {
		if (usuarioRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email ya está en uso.");
		}

		Usuario user = new Usuario();
		user.setNombre(request.getNombre());
		user.setApellidos(request.getApellidos());
		user.setEmail(request.getEmail());
		user.setContrasena(passwordEncoder.encode(request.getContrasena()));
		user.getRoles().add(Roles.ROLE_USER);
		usuarioRepository.save(user);
		String jwt = jwtService.generateToken(user);
		return new JwtAuthenticationResponse(jwt);
	}

	@Override
	public JwtAuthenticationResponse signin(LoginRequest request) {
		@SuppressWarnings("unused")
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getContrasena()));

		// SecurityContextHolder.getContext().setAuthentication(authentication);

		Usuario user = usuarioRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Email o contraseña inválidos."));
		String jwt = jwtService.generateToken(user);
		return JwtAuthenticationResponse.builder().token(jwt).build();
	}
}
