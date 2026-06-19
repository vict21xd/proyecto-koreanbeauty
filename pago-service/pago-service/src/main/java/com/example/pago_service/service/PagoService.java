package com.example.pago_service.service;

import com.example.pago_service.model.Pago;
import com.example.pago_service.repository.PagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PagoService {

    private final PagoRepository repo;

    public PagoService(PagoRepository repo) {
        this.repo = repo;
    }

    public Pago guardar(Pago pago) {

        log.info("Creando pago idCompra={}", pago.getIdCompra());

        validar(pago);

        pago.setFechaPago(new Date());

        return repo.save(pago);
    }

    public List<Pago> listar() {
        log.info("Listando pagos");
        return repo.findAll();
    }

    public Pago obtenerPorId(Long id) {
        log.info("Buscando pago id={}", id);

        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Pago no encontrado id={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Pago no encontrado"
                    );
                });
    }

    public Pago actualizar(Long id, Pago pago) {

        log.info("Actualizando pago id={}", id);

        Pago existente = obtenerPorId(id);

        pago.setId(existente.getId());

        validar(pago);

        return repo.save(pago);
    }

    public void eliminar(Long id) {
        log.info("Eliminando pago id={}", id);
        repo.delete(obtenerPorId(id));
    }

    public List<Pago> buscarPorEstado(String estado) {
        log.info("Buscando pagos estado={}", estado);
        return repo.findByEstadoIgnoreCase(estado);
    }

    private void validar(Pago pago) {

        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Monto inválido");
        }

        if (pago.getMetodoPago() == null || pago.getMetodoPago().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Método de pago obligatorio");
        }

        if (pago.getEstado() == null || pago.getEstado().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado obligatorio");
        }
    }
}