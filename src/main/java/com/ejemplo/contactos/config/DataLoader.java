package com.ejemplo.contactos.config;

import com.ejemplo.contactos.model.Contacto;
import com.ejemplo.contactos.model.Usuario;
import com.ejemplo.contactos.repository.ContactoRepository;
import com.ejemplo.contactos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    @Bean
    public CommandLineRunner initDatabase(
            ContactoRepository contactoRepository,
            UsuarioRepository usuarioRepository) {
        return args -> {
            // Usuario por defecto
            if (usuarioRepository.count() == 0) {
                log.info("Creando usuario administrador de demostración...");
                usuarioRepository.save(Usuario.builder()
                        .nombre("Administrador")
                        .email("admin@agenda.com")
                        .password("admin123")
                        .build());
                log.info("Usuario creado: admin@agenda.com / admin123");
            }

            // Contactos iniciales
            if (contactoRepository.count() == 0) {
                log.info("Cargando datos iniciales de contactos...");
                contactoRepository.saveAll(List.of(
                        Contacto.builder()
                                .nombre("Carlos Mendoza")
                                .numero("+34 612 345 678")
                                .email("carlos.mendoza@ejemplo.com")
                                .provincia("Madrid")
                                .build(),
                        Contacto.builder()
                                .nombre("Ana García")
                                .numero("+34 687 654 321")
                                .email("ana.garcia@ejemplo.com")
                                .provincia("Barcelona")
                                .build(),
                        Contacto.builder()
                                .nombre("Laura Rodríguez")
                                .numero("+34 655 112 233")
                                .email("laura.rodriguez@ejemplo.com")
                                .provincia("Valencia")
                                .build(),
                        Contacto.builder()
                                .nombre("David Fernández")
                                .numero("+34 699 887 766")
                                .email("david.fernandez@ejemplo.com")
                                .provincia("Sevilla")
                                .build()
                ));
                log.info("Contactos de demostración cargados correctamente.");
            }
        };
    }
}
