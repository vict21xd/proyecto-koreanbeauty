package com.example.producto_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.producto_service.model.Producto;
import com.example.producto_service.repository.ProductoRepository;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        producto = Producto.builder()
                .id(1L)
                .nombre("Crema Facial")
                .descripcion("Hidratante")
                .precio(15000.0)
                .stock(10)
                .categoria("Skincare")
                .build();
    }

    @Test
    void givenProductoValidoWhenGuardarThenRetornaProducto() {

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(producto);

        Producto resultado =
                productoService.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Crema Facial", resultado.getNombre());

        verify(productoRepository, times(1))
                .save(any(Producto.class));
    }

    @Test
    void givenProductosWhenListarThenRetornaLista() {

        when(productoRepository.findAll())
                .thenReturn(List.of(producto));

        List<Producto> resultado =
                productoService.listar();

        assertEquals(1, resultado.size());

        verify(productoRepository, times(1))
                .findAll();
    }

    @Test
    void givenIdExistenteWhenBuscarThenRetornaProducto() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        Producto resultado =
                productoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void givenIdInexistenteWhenBuscarThenLanzaExcepcion() {

        when(productoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> productoService.obtenerPorId(99L)
        );
    }

    @Test
    void givenNombreWhenBuscarThenRetornaCoincidencias() {

        when(productoRepository
                .findByNombreContainingIgnoreCase("Crema"))
                .thenReturn(List.of(producto));

        List<Producto> resultado =
                productoService.buscarPorNombre("Crema");

        assertEquals(1, resultado.size());
    }

    @Test
    void givenProductoExistenteWhenEliminarThenElimina() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        productoService.eliminar(1L);

        verify(productoRepository)
                .delete(producto);
    }

    @Test
    void calcularIVA() {

        Double resultado =
                productoService.calcularPrecioConIVA(10000.0);

        assertEquals(11900.0, resultado);
    }

    @Test
    void precioInvalido() {

        producto.setPrecio(-100.0);

        assertThrows(
                ResponseStatusException.class,
                () -> productoService.guardar(producto)
        );
    }

    @Test
    void categoriaVacia() {

        producto.setCategoria("");

        assertThrows(
                ResponseStatusException.class,
                () -> productoService.guardar(producto)
        );
    }
}