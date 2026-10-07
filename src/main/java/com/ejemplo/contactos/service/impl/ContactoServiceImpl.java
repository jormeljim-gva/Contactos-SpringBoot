package com.ejemplo.contactos.service.impl;

import com.ejemplo.contactos.dto.ActualizarContactoDTO;
import com.ejemplo.contactos.dto.ContactoDTO;
import com.ejemplo.contactos.dto.CrearContactoDTO;
import com.ejemplo.contactos.exception.BadRequestException;
import com.ejemplo.contactos.exception.ResourceNotFoundException;
import com.ejemplo.contactos.model.Contacto;
import com.ejemplo.contactos.repository.ContactoRepository;
import com.ejemplo.contactos.service.ContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactoServiceImpl implements ContactoService {

    private final ContactoRepository contactoRepository;

    @Override
    @Transactional
    public ContactoDTO crearContacto(CrearContactoDTO dto) {
        if (contactoRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Ya existe un contacto con el email: " + dto.getEmail());
        }

        Contacto contacto = Contacto.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();

        Contacto guardado = contactoRepository.save(contacto);
        return mapToDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactoDTO obtenerContactoPorId(Long id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contacto", "id", id));
        return mapToDTO(contacto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactoDTO> obtenerTodosLosContactos(Pageable pageable) {
        return contactoRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactoDTO> buscarContactos(String query, Pageable pageable) {
        return contactoRepository
                .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        query, query, query, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional
    public ContactoDTO actualizarContacto(Long id, ActualizarContactoDTO dto) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contacto", "id", id));

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (contactoRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new BadRequestException("Ya existe otro contacto con el email: " + dto.getEmail());
            }
            contacto.setEmail(dto.getEmail());
        }

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            contacto.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null && !dto.getApellido().isBlank()) {
            contacto.setApellido(dto.getApellido());
        }
        if (dto.getTelefono() != null) {
            contacto.setTelefono(dto.getTelefono());
        }
        if (dto.getDireccion() != null) {
            contacto.setDireccion(dto.getDireccion());
        }

        Contacto actualizado = contactoRepository.save(contacto);
        return mapToDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarContacto(Long id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contacto", "id", id));
        contactoRepository.delete(contacto);
    }

    private ContactoDTO mapToDTO(Contacto contacto) {
        return ContactoDTO.builder()
                .id(contacto.getId())
                .nombre(contacto.getNombre())
                .apellido(contacto.getApellido())
                .email(contacto.getEmail())
                .telefono(contacto.getTelefono())
                .direccion(contacto.getDireccion())
                .fechaCreacion(contacto.getFechaCreacion())
                .fechaActualizacion(contacto.getFechaActualizacion())
                .build();
    }
}
