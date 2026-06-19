package com.example.resena_service.service;

import com.example.resena_service.dto.ResenaDto;
import com.example.resena_service.exception.ResourceNotFoundException;
import com.example.resena_service.model.Resena;
import com.example.resena_service.repository.ResenaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResenaServiceTest {

    @Mock
    private ResenaRepository repository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ResenaService service;

    private Resena resena;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        resena = Resena.builder()
                .id(1L)
                .clienteId(1L)
                .productoId(1L)
                .puntuacion(5)
                .comentario("Excelente")
                .fecha(LocalDate.now())
                .build();
    }

    @Test
    void listar_ok() {

        when(repository.findAll()).thenReturn(List.of(resena));

        List<ResenaDto> resultado = service.listar();

        assertEquals(1, resultado.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void buscar_ok() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(resena));

        ResenaDto resultado = service.buscarPorId(1L);

        assertNotNull(resultado);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscar_noExiste() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.buscarPorId(99L)
        );
    }

    @Test
    void eliminar_ok() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(resena));

        service.eliminar(1L);

        verify(repository).delete(resena);
    }

    @Test
    void guardar_puntuacionInvalida() {

        ResenaDto dto = ResenaDto.builder()
                .clienteId(1L)
                .productoId(1L)
                .puntuacion(6)
                .comentario("Malo")
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.guardar(dto)
        );
    }

    @Test
    void actualizar_ok() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(resena));

        when(repository.save(any(Resena.class)))
                .thenReturn(resena);

        ResenaDto dto = ResenaDto.builder()
                .puntuacion(4)
                .comentario("Bien")
                .build();

        ResenaDto resultado = service.actualizar(1L, dto);

        assertNotNull(resultado);

        verify(repository).save(any(Resena.class));
    }
}