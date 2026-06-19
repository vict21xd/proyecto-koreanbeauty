package com.example.cliente_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cliente_service.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByTipoPiel(String tipoPiel);

}