package com.taemoi.project.servicios.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taemoi.project.dtos.UsuarioDTO;
import com.taemoi.project.repositorios.UsuarioRepository;
import com.taemoi.project.servicios.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

        	@Override
            public UserDetails loadUserByUsername(String nombre) {
                return usuarioRepository.findByEmail(nombre)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            }
        };
    }

    @Override
	public List<UsuarioDTO> obtenerTodos() {
		List<UsuarioDTO> usuarios =  usuarioRepository.findAll().stream()
			    .map(usuario -> new UsuarioDTO(usuario.getNombre(), usuario.getApellidos(), usuario.getEmail(), usuario.getRoles().toString()))
			    .collect(Collectors.toList());
		 return usuarios;
	}
}
