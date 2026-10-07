package com.ejemplo.contactos.repository;

import com.ejemplo.contactos.model.Contacto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    Optional<Contacto> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Contacto> findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCaseOrProvinciaContainingIgnoreCaseOrNumeroContainingIgnoreCase(
            String nombre, String email, String provincia, String numero, Pageable pageable);
}
