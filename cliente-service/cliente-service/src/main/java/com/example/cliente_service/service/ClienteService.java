package com.example.cliente_service.service;

import com.example.cliente_service.exception.ClienteNotFoundException;
import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.repository.ClienteRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private static final Logger logger =
            LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente guardar(Cliente cliente) {

        logger.info("Creando cliente: {}", cliente.getNombre());

        validar(cliente);

        Cliente saved = clienteRepository.save(cliente);

        logger.info("Cliente creado ID: {}", saved.getId());

        return saved;
    }

    public List<Cliente> listar() {

        logger.info("Listando clientes");

        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Long id) {

        logger.info("Buscando cliente ID: {}", id);

        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cliente no encontrado ID: {}", id);
                    return new ClienteNotFoundException(id);
                });
    }

    public Cliente actualizar(Long id, Cliente cliente) {

        logger.info("Actualizando cliente ID: {}", id);

        Cliente existente = obtenerPorId(id);

        cliente.setId(existente.getId());

        validar(cliente);

        Cliente updated = clienteRepository.save(cliente);

        logger.info("Cliente actualizado ID: {}", updated.getId());

        return updated;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando cliente ID: {}", id);

        Cliente existente = obtenerPorId(id);

        clienteRepository.delete(existente);

        logger.info("Cliente eliminado ID: {}", id);
    }

    public List<Cliente> listarPorTipoPiel(String tipoPiel) {

        logger.info("Buscando clientes por tipo piel: {}", tipoPiel);

        return clienteRepository.findByTipoPiel(tipoPiel);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia cliente ID: {}", id);

        return clienteRepository.existsById(id);
    }

    private void validar(Cliente cliente) {

        logger.info("Validando cliente: {}", cliente.getNombre());

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {

            logger.error("Nombre obligatorio");

            throw new IllegalArgumentException(
                    "Nombre obligatorio");
        }

        if (cliente.getNombre().equalsIgnoreCase("string")) {

            logger.error("Nombre inválido");

            throw new IllegalArgumentException(
                    "Nombre inválido");
        }

        if (cliente.getCorreo() == null ||
                !cliente.getCorreo().contains("@")) {

            logger.error("Correo inválido");

            throw new IllegalArgumentException(
                    "Correo inválido");
        }

        if (cliente.getTelefono() == null ||
                cliente.getTelefono().length() < 8) {

            logger.error("Teléfono inválido");

            throw new IllegalArgumentException(
                    "Teléfono inválido");
        }

        if (cliente.getTipoPiel() == null ||
                cliente.getTipoPiel().isBlank()) {

            logger.error("Tipo de piel obligatorio");

            throw new IllegalArgumentException(
                    "Tipo de piel obligatorio");
        }

        if (cliente.getTipoPiel().equalsIgnoreCase("string")) {

            logger.error("Tipo de piel inválido");

            throw new IllegalArgumentException(
                    "Tipo de piel inválido");
        }
    }
}