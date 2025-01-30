package com.example.clienteStorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clienteStorage.entity.Cliente;

public interface ClienteRepository extends JpaRepository <Cliente, Long> {

	
}
