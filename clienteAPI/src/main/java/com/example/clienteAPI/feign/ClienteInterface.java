package com.example.clienteAPI.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.clienteAPI.web.dto.ClienteCreateDto;
import com.example.clienteAPI.web.dto.ClienteResponseDto;

@FeignClient(name = "clienteStorage", url = "http://localhost:9993/api/v1/clientes")
public interface ClienteInterface {
	
	@PostMapping
	ClienteResponseDto createCliente(@RequestBody ClienteCreateDto createDto);

    @PutMapping("/{id}")
    ClienteResponseDto updateCliente(@PathVariable("id") Long id, @RequestBody ClienteCreateDto createDto);

    @GetMapping
    List<ClienteResponseDto> readClientes();

    @GetMapping("/{id}")
    ClienteResponseDto getClientebyId(@PathVariable("id") Long id);

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable("id") Long id);

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> inactivateCC(@PathVariable("id") Long id);

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> activateCC(@PathVariable("id") Long id);
}
