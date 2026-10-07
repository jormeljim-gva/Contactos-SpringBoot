package com.ejemplo.contactos.service.impl;

import com.ejemplo.contactos.dto.AuthResponse;
import com.ejemplo.contactos.dto.LoginRequest;
import com.ejemplo.contactos.dto.RegistroRequest;
import com.ejemplo.contactos.exception.BadRequestException;
import com.ejemplo.contactos.model.Usuario;
import com.ejemplo.contactos.repository.UsuarioRepository;
import com.ejemplo.contactos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Credenciales inválidas: usuario no encontrado"));

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Credenciales inválidas: contraseña incorrecta");
        }

        String tokenSimulado = UUID.randomUUID().toString();

        return AuthResponse.builder()
                .token(tokenSimulado)
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .mensaje("Inicio de sesión exitoso")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Ya existe una cuenta con el email: " + request.getEmail());
        }

        Usuario nuevoUsuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        usuarioRepository.save(nuevoUsuario);
        String tokenSimulado = UUID.randomUUID().toString();

        return AuthResponse.builder()
                .token(tokenSimulado)
                .email(nuevoUsuario.getEmail())
                .nombre(nuevoUsuario.getNombre())
                .mensaje("Usuario registrado e inicio de sesión automático")
                .build();
    }
}
