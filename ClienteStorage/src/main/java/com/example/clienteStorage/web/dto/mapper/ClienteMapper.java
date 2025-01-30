package com.example.clienteStorage.web.dto.mapper;  // Pacote onde a classe está localizada

import java.util.List;
import java.util.stream.Collectors;

// Importando as classes necessárias para o mapeamento e configuração do componente
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.clienteStorage.entity.Cliente;
import com.example.clienteStorage.web.dto.ClienteCreateDto;
import com.example.clienteStorage.web.dto.ClienteResponseDto;

import lombok.RequiredArgsConstructor;  // Anotação do Lombok para injeção automática do construtor

@Component  // Anotação que indica que essa classe é um componente gerenciado pelo Spring
@RequiredArgsConstructor  // Anotação do Lombok para gerar automaticamente o construtor com parâmetros finais
public class ClienteMapper {

    private final ModelMapper modelMapper;  // Injeção de dependência do ModelMapper para conversão de objetos
    
    // Método genérico para mapear um objeto de um tipo para outro
    public void map(Object source, Object destination) {
        modelMapper.map(source, destination);  // O ModelMapper faz a conversão do 'source' para 'destination'
    }

    // Método para converter um ClienteCreateDto em um Cliente
    public Cliente toCliente(ClienteCreateDto createDto) {
        return modelMapper.map(createDto, Cliente.class);  // Mapeia o DTO para a entidade Cliente
    }

    // Método para converter um Cliente em um ClienteResponseDto
    public ClienteResponseDto toDto(Cliente cliente) {
        return modelMapper.map(cliente, ClienteResponseDto.class);  // Mapeia a entidade Cliente para o DTO de resposta
    }
    
    public List<ClienteResponseDto> toListDto(List<Cliente> clientes){
    	return clientes.stream().map(cliente-> toDto(cliente)).collect(Collectors.toList());
    }
}