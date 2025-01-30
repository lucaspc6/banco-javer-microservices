package com.example.clienteAPI.web.dto.mapper;  // Pacote onde o código está localizado

// Importando as classes necessárias para configurar o ModelMapper no Spring
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // Anotação que indica que esta classe contém configurações para o Spring
public class configMapper {
    
    // Método que define e cria um bean de ModelMapper que será gerenciado pelo Spring
    @Bean
    public ModelMapper modelMapper() {
        
        // Criando uma nova instância do ModelMapper
        ModelMapper modelMapper = new ModelMapper();
        
        // Configurando o ModelMapper para ignorar os campos com valor null durante o mapeamento
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        
        // Retorna o objeto modelMapper, que será gerenciado pelo Spring
        return modelMapper;
    }
}