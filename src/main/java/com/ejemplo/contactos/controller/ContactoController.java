package com.ejemplo.contactos.controller;

import com.ejemplo.contactos.dto.ActualizarContactoDTO;
import com.ejemplo.contactos.dto.ContactoDTO;
import com.ejemplo.contactos.dto.CrearContactoDTO;
import com.ejemplo.contactos.service.ContactoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contactos")
@RequiredArgsConstructor
@Tag(name = "Contactos", description = "Endpoints para la gestión de contactos")
@CrossOrigin(origins = "*")
public class ContactoController {

    private final ContactoService contactoService;

    @PostMapping
    @Operation(summary = "Crear un nuevo contacto")
    public ResponseEntity<ContactoDTO> crearContacto(@Valid @RequestBody CrearContactoDTO dto) {
        ContactoDTO nuevoContacto = contactoService.crearContacto(dto);
        return new ResponseEntity<>(nuevoContacto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un contacto por su ID")
    public ResponseEntity<ContactoDTO> obtenerPorId(@PathVariable Long id) {
        ContactoDTO contacto = contactoService.obtenerContactoPorId(id);
        return ResponseEntity.ok(contacto);
    }

    @GetMapping
    @Operation(summary = "Obtener listado paginado de contactos")
    public ResponseEntity<Page<ContactoDTO>> obtenerTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(contactoService.obtenerTodosLosContactos(pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar contactos por término (nombre, apellido o email)")
    public ResponseEntity<Page<ContactoDTO>> buscar(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        return ResponseEntity.ok(contactoService.buscarContactos(query, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un contacto")
    public ResponseEntity<ContactoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarContactoDTO dto) {
        ContactoDTO actualizado = contactoService.actualizarContacto(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un contacto por su ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        contactoService.eliminarContacto(id);
        return ResponseEntity.noContent().build();
    }
}
