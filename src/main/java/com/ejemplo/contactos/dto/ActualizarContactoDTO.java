package com.ejemplo.contactos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarContactoDTO {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(min = 3, max = 30, message = "El número debe tener entre 3 y 30 caracteres")
    private String numero;

    @Email(message = "Debe proporcionar un email válido")
    private String email;

    @Size(min = 2, max = 100, message = "La provincia debe tener entre 2 y 100 caracteres")
    private String provincia;
}
