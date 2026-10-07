package com.ejemplo.contactos.service;

import com.ejemplo.contactos.dto.AuthResponse;
import com.ejemplo.contactos.dto.LoginRequest;
import com.ejemplo.contactos.dto.RegistroRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse registrar(RegistroRequest request);
}
