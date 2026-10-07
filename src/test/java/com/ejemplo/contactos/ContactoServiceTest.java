package com.ejemplo.contactos;

import com.ejemplo.contactos.dto.ContactoDTO;
import com.ejemplo.contactos.dto.CrearContactoDTO;
import com.ejemplo.contactos.exception.BadRequestException;
import com.ejemplo.contactos.model.Contacto;
import com.ejemplo.contactos.repository.ContactoRepository;
import com.ejemplo.contactos.service.impl.ContactoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactoServiceTest {

    @Mock
    private ContactoRepository contactoRepository;

    @InjectMocks
    private ContactoServiceImpl contactoService;

    @Test
    void crearContactoExitoso() {
        CrearContactoDTO dto = CrearContactoDTO.builder()
                .nombre("Juan Pérez")
                .numero("+34 600 000 000")
                .email("juan.perez@ejemplo.com")
                .provincia("Madrid")
                .build();

        Contacto contactoGuardado = Contacto.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .numero("+34 600 000 000")
                .email("juan.perez@ejemplo.com")
                .provincia("Madrid")
                .build();

        when(contactoRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(contactoRepository.save(any(Contacto.class))).thenReturn(contactoGuardado);

        ContactoDTO resultado = contactoService.crearContacto(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("Madrid", resultado.getProvincia());
        verify(contactoRepository, times(1)).save(any(Contacto.class));
    }

    @Test
    void crearContactoEmailDuplicadoLanzaExcepcion() {
        CrearContactoDTO dto = CrearContactoDTO.builder()
                .nombre("Juan Pérez")
                .numero("+34 600 000 000")
                .email("duplicado@ejemplo.com")
                .provincia("Madrid")
                .build();

        when(contactoRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> contactoService.crearContacto(dto));
        verify(contactoRepository, never()).save(any(Contacto.class));
    }
}
