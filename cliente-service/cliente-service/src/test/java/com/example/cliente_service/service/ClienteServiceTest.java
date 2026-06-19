package com.example.cliente_service.service;

import com.example.cliente_service.exception.ClienteNotFoundException;
import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.repository.ClienteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        cliente = Cliente.builder()
                .id(1L)
                .nombre("Fathia")
                .correo("fathia@gmail.com")
                .telefono("987654321")
                .tipoPiel("Mixta")
                .build();
    }

    @Test
    void givenClienteValidoWhenGuardarThenRetornaCliente() {

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        Cliente resultado = clienteService.guardar(cliente);

        assertNotNull(resultado);
        assertEquals("Fathia", resultado.getNombre());

        verify(clienteRepository, times(1))
                .save(any(Cliente.class));
    }

    @Test
    void givenClientesWhenListarThenRetornaLista() {

        when(clienteRepository.findAll())
                .thenReturn(List.of(cliente));

        List<Cliente> resultado =
                clienteService.listar();

        assertEquals(1, resultado.size());
    }

    @Test
    void givenClienteExistenteWhenBuscarThenRetornaCliente() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        Cliente resultado =
                clienteService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void givenClienteNoExistenteWhenBuscarThenLanzaExcepcion() {

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ClienteNotFoundException.class,
                () -> clienteService.obtenerPorId(99L)
        );
    }

    @Test
    void givenClienteExistenteWhenActualizarThenRetornaActualizado() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        Cliente actualizado =
                clienteService.actualizar(1L, cliente);

        assertNotNull(actualizado);

        verify(clienteRepository)
                .save(any(Cliente.class));
    }

    @Test
    void givenClienteExistenteWhenEliminarThenElimina() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        doNothing()
                .when(clienteRepository)
                .delete(cliente);

        clienteService.eliminar(1L);

        verify(clienteRepository)
                .delete(cliente);
    }

    @Test
    void givenTipoPielWhenBuscarThenRetornaLista() {

        when(clienteRepository.findByTipoPiel("Mixta"))
                .thenReturn(List.of(cliente));

        List<Cliente> resultado =
                clienteService.listarPorTipoPiel("Mixta");

        assertEquals(1, resultado.size());
    }
}