package com.example.compra_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.compra_service.exception.ResourceNotFoundException;
import com.example.compra_service.model.Compra;
import com.example.compra_service.model.DetalleCompra;
import com.example.compra_service.repository.CompraRepository;

class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraService compraService;

    private Compra compra;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        DetalleCompra detalle = new DetalleCompra();
        detalle.setIdProducto(1L);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(10000.0);

        compra = Compra.builder()
                .id(1L)
                .idCliente(1L)
                .fechaCompra(new Date())
                .detalles(List.of(detalle))
                .build();
    }

    @Test
    void givenComprasWhenListarThenRetornaLista() {

        when(compraRepository.findAll())
                .thenReturn(List.of(compra));

        List<Compra> resultado = compraService.listar();

        assertEquals(1, resultado.size());
        verify(compraRepository).findAll();
    }

    @Test
    void givenIdExistenteWhenBuscarThenRetornaCompra() {

        when(compraRepository.findById(1L))
                .thenReturn(Optional.of(compra));

        Compra resultado = compraService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void givenIdInexistenteWhenBuscarThenLanzaExcepcion() {

        when(compraRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> compraService.obtenerPorId(99L)
        );
    }

    @Test
    void givenCompraExistenteWhenEliminarThenElimina() {

        when(compraRepository.findById(1L))
                .thenReturn(Optional.of(compra));

        doNothing().when(compraRepository).delete(compra);

        compraService.eliminar(1L);

        verify(compraRepository).delete(compra);
    }

    @Test
    void givenCompraValidaWhenGuardarThenRetornaCompra() {

        when(compraRepository.save(any(Compra.class)))
                .thenReturn(compra);

        Compra resultado = compraService.guardar(compra);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCliente());

        verify(compraRepository).save(any(Compra.class));
    }
}