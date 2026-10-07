package com.ejemplo.contactos.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactoDTO {
    private Long id;
    private String nombre;
    private String numero;
    private String email;
    private String provincia;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
