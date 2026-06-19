package com.example.categoria_service.service;

import com.example.categoria_service.dto.CategoriaDto;
import com.example.categoria_service.exception.ResourceNotFoundException;
import com.example.categoria_service.model.Categoria;
import com.example.categoria_service.repository.CategoriaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService service;

    @Test
    void listarCategorias_OK() {

        List<Categoria> lista = List.of(
                Categoria.builder().id(1L).nombre("SERUMS").build(),
                Categoria.builder().id(2L).nombre("TONICOS").build()
        );

        when(repository.findAll()).thenReturn(lista);

        List<CategoriaDto> resultado = service.listar();

        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void buscarPorId_OK() {

        Categoria categoria = Categoria.builder()
                .id(1L)
                .nombre("SERUMS")
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(categoria));

        CategoriaDto resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("SERUMS", resultado.getNombre());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorId_NOT_FOUND() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.buscarPorId(1L)
        );

        verify(repository).findById(1L);
    }

    @Test
    void guardarCategoria_OK() {

        CategoriaDto dto = CategoriaDto.builder()
                .nombre("SERUMS")
                .descripcion("Test")
                .build();

        when(repository.existsByNombre(anyString()))
                .thenReturn(false);

        Categoria saved = Categoria.builder()
                .id(1L)
                .nombre("SERUMS")
                .descripcion("Test")
                .build();

        when(repository.save(any(Categoria.class)))
                .thenReturn(saved);

        CategoriaDto resultado = service.guardar(dto);

        assertNotNull(resultado);
        assertEquals("SERUMS", resultado.getNombre());

        verify(repository).save(any(Categoria.class));
    }

    @Test
    void guardarCategoria_Duplicada() {

    
        CategoriaDto dto = CategoriaDto.builder()
                .nombre("SERUMS")
                .build();

        when(repository.existsByNombre(anyString()))
                .thenReturn(true);

        
        assertThrows(
                IllegalArgumentException.class,
                () -> service.guardar(dto)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void guardarCategoria_NombreString() {

     CategoriaDto dto = CategoriaDto.builder()
                .nombre("string")
                .descripcion("Prueba")
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.guardar(dto)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void eliminarCategoria_OK() {

        Categoria categoria = Categoria.builder()
                .id(1L)
                .nombre("TONICOS")
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(categoria));

        service.eliminar(1L);

        verify(repository).delete(categoria);
    }

    @Test
    void eliminarCategoria_SerumsEnUso() {

        
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nombre("SÉRUMS")
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(categoria));

       
        assertThrows(
                IllegalArgumentException.class,
                () -> service.eliminar(1L)
        );

        verify(repository, never()).delete(any());
    }
}