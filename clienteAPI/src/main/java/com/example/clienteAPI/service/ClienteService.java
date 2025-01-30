package com.example.clienteAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.clienteAPI.exception.EntityNotFoundException;
import com.example.clienteAPI.exception.TelefoneUniqueViolationException;
import com.example.clienteAPI.feign.ClienteInterface;
import com.example.clienteAPI.web.dto.ClienteCreateDto;
import com.example.clienteAPI.web.dto.ClienteResponseDto;

import feign.FeignException;

@Service
public class ClienteService {

    @Autowired
    private ClienteInterface clienteInterface;

//    public ClienteResponseDto createCliente(ClienteCreateDto createDto) {
//        try {
//        	return clienteInterface.createCliente(createDto);
//    		} catch (org.springframework.dao.DataIntegrityViolationException ex){
//    			throw new TelefoneUniqueViolationException(String.format("Este telefone já foi cadastrado", createDto.getTelefone()));
//    		}
//    }
    
    public ClienteResponseDto createCliente(ClienteCreateDto createDto) {
        try {
        	return clienteInterface.createCliente(createDto);
        } catch (FeignException ex) {
            // Tratamento de exceções específicas do Feign Client
            if (ex.status() == 409) {
                // Lançando exceção personalizada para erro 409
                throw new TelefoneUniqueViolationException(String.format("Este telefone já foi cadastrado", createDto.getTelefone()));
            }
            // Relançar exceções para serem tratadas pelo ApiExceptionHandler
            throw ex;
        }
    }

    public ClienteResponseDto updateCliente(Long id, ClienteCreateDto createDto) {
        return clienteInterface.updateCliente(id, createDto);
    }

    public List<ClienteResponseDto> readClientes() {
        return clienteInterface.readClientes();
    }

    public ClienteResponseDto getClientebyId(Long id) {
        try {
        	return clienteInterface.getClientebyId(id);
        } catch (FeignException ex) {
            // Tratamento de exceções específicas do Feign Client
            if (ex.status() == 404) {
                // Lançando exceção personalizada para erro 409
                throw new EntityNotFoundException(String.format("Cliente id=%s não encontrado", id));
            }
            // Relançar exceções para serem tratadas pelo ApiExceptionHandler
            throw ex;
        }
    }

    public ResponseEntity<Void> deleteCliente(Long id) {
    	return clienteInterface.deleteCliente(id);
    }

    public ResponseEntity<Void> inactivateCC(Long id) {
    	return clienteInterface.inactivateCC(id);
    }

    public ResponseEntity<Void> activateCC(Long id) {
    	return clienteInterface.activateCC(id);
    }
     
    
    public Float calcularScore(ClienteResponseDto clienteResponseDto) {
    	 // Recuperando o saldo diretamente do DTO
        Float saldo_cc = clienteResponseDto.getSaldo_cc();

        // Calculando o score de crédito com base no saldo recuperado do DTO
        return saldo_cc * 0.1f;
    }
}