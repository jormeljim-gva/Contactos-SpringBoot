package com.ejemplo.contactos.service;

import com.ejemplo.contactos.dto.ActualizarContactoDTO;
import com.ejemplo.contactos.dto.ContactoDTO;
import com.ejemplo.contactos.dto.CrearContactoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactoService {

    ContactoDTO crearContacto(CrearContactoDTO dto);

    ContactoDTO obtenerContactoPorId(Long id);

    Page<ContactoDTO> obtenerTodosLosContactos(Pageable pageable);

    Page<ContactoDTO> buscarContactos(String query, Pageable pageable);

    ContactoDTO actualizarContacto(Long id, ActualizarContactoDTO dto);

    void eliminarContacto(Long id);
}
