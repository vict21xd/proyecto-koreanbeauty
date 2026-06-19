package com.example.pago_service.service;

import com.example.pago_service.model.Pago;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PagoServiceTest {

    @Test
    void crearPago() {

        Pago pago = Pago.builder()
                .idCompra(1L)
                .monto(10000.0)
                .metodoPago("TARJETA")
                .estado("PAGADO")
                .build();

        assertEquals(1L, pago.getIdCompra());
        assertEquals("PAGADO", pago.getEstado());
        assertEquals("TARJETA", pago.getMetodoPago());
    }
}