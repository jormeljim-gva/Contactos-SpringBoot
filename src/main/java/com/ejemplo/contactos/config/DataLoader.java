package com.ejemplo.contactos.config;

import com.ejemplo.contactos.model.Contacto;
import com.ejemplo.contactos.repository.ContactoRepository;
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
    public CommandLineRunner initDatabase(ContactoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                log.info("Cargando datos iniciales de contactos...");
                repository.saveAll(List.of(
                        Contacto.builder()
                                .nombre("Carlos")
                                .apellido("Mendoza")
                                .email("carlos.mendoza@ejemplo.com")
                                .telefono("+34 612 345 678")
                                .direccion("Calle Mayor 12, Madrid")
                                .build(),
                        Contacto.builder()
                                .nombre("Ana")
                                .apellido("García")
                                .email("ana.garcia@ejemplo.com")
                                .telefono("+34 687 654 321")
                                .direccion("Av. Diagonal 450, Barcelona")
                                .build(),
                        Contacto.builder()
                                .nombre("Laura")
                                .apellido("Rodríguez")
                                .email("laura.rodriguez@ejemplo.com")
                                .telefono("+34 655 112 233")
                                .direccion("Calle Gran Vía 88, Valencia")
                                .build()
                ));
                log.info("Contactos iniciales cargados correctamente.");
            }
        };
    }
}
