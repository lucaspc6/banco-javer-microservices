package com.example.clienteAPI.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clienteAPI.service.ClienteService;
import com.example.clienteAPI.web.dto.ClienteCreateDto;
import com.example.clienteAPI.web.dto.ClienteResponseDto;
import com.example.clienteAPI.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Clientes - Exposição de EndPoints", description = "Interaja com a segunda aplicação: crie, atualize, consulte clientes e calcule o score de crédito com facilidade.")
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Criação de um novo cliente", description = "Adicione um novo cliente ao sistema com dados essenciais.",
			responses = {
				        @ApiResponse(responseCode = "200", description = "Cliente adicionado com sucesso! 🎉",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),

				        @ApiResponse(responseCode = "500", description = "O campo 'Correntista' é obrigatório e deve ser verdadeiro ou falso.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "400", description = "Nome é obrigatório e não pode ser vazio!",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "500", description = "O nome pode conter apenas letras e espaços.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "400", description = "Saldo é obrigatório para criação do cliente.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "400", description = "Telefone deve ter exatamente 11 dígitos.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "400", description = "Saldo negativo não é permitido.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "409", description = "Número de telefone já cadastrado, escolha outro.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "500", description = "Erro inesperado, por favor tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
			}		
	)
    @PostMapping
    public ResponseEntity<ClienteResponseDto> createCliente(@RequestBody ClienteCreateDto createDto) {
        ClienteResponseDto response = clienteService.createCliente(createDto);
        response.setScore_credito(clienteService.calcularScore(response));
        return ResponseEntity.ok(response);
    }
    
   
    
    @Operation(summary = "Atualização de dados do cliente", description = "Atualize os dados de um cliente já cadastrado no sistema.",
			responses = {
				        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso! ✅",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),

				        @ApiResponse(responseCode = "500", description = "Nome não pode ser vazio. Favor inserir um nome válido.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "500", description = "O nome só pode conter letras e espaços.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "400", description = "Saldo negativo não é permitido, por favor corrija o valor.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "409", description = "O número de telefone já está em uso. Escolha outro.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "500", description = "Telefone deve ter 11 dígitos. Por favor, verifique.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "500", description = "Erro inesperado. Por favor, tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

				        @ApiResponse(responseCode = "404", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
			}		
	)
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> updateCliente(@PathVariable("id") Long id, @RequestBody ClienteCreateDto createDto) {
        ClienteResponseDto response = clienteService.updateCliente(id, createDto);
        response.setScore_credito(clienteService.calcularScore(response));
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Listar todos os clientes", description  = "Recupere a lista completa de clientes cadastrados no sistema.",
			responses = {
				        @ApiResponse(responseCode = "200", description = "Clientes listados com sucesso! 📋",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
			}		
	)
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> readClientes() {
        List<ClienteResponseDto> response = clienteService.readClientes();
        
        response.forEach(cliente -> 
        	cliente.setScore_credito(clienteService.calcularScore(cliente))
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Recuperar um cliente pelo ID", description  = "Obtenha as informações de um cliente específico a partir de seu ID único.",
			responses = {
				        @ApiResponse(responseCode = "200", description = "Cliente recuperado com sucesso! 🎯",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),

				        @ApiResponse(responseCode = "404", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}		
	)
    @GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDto> getClientebyId(@PathVariable("id") Long id) {
	      ClienteResponseDto response = clienteService.getClientebyId(id);	
	      response.setScore_credito(clienteService.calcularScore(response));
	      return ResponseEntity.ok(response);
	  }
    
    @Operation(summary = "Deletar um cliente", description  = "Exclua um cliente do sistema pelo seu ID único.",
			responses = {
						@ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso! 🗑️"),
					
				        @ApiResponse(responseCode = "500", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
				        
				        @ApiResponse(responseCode = "400", description = "A conta corrente está ativa e não pode ser excluída. Desative-a primeiro.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}		
	)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable("id") Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Desativar a conta corrente do cliente", description  = "Desative a conta corrente do cliente utilizando seu ID. Certifique-se de que o saldo esteja zerado.",
			responses = {
						@ApiResponse(responseCode = "204", description = "Conta corrente do cliente desativada com sucesso. 💳❌"),
					
				        @ApiResponse(responseCode = "404", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
				        
				        @ApiResponse(responseCode = "400", description = "Saldo deve estar zerado para desativar a conta.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}		
	)
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> inactivateCC(@PathVariable("id") Long id) {
        clienteService.inactivateCC(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Ativar a conta corrente do cliente", description  = "Ative a conta corrente do cliente novamente utilizando seu ID.",
			responses = {
						@ApiResponse(responseCode = "204", description = "Conta corrente do cliente ativada com sucesso. 💳✔️"),
					
				        @ApiResponse(responseCode = "404", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}		
	)
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> activateCC(@PathVariable("id") Long id) {
    	clienteService.activateCC(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Calcular Score_Credito do cliente pelo ID", description  = "Obtenha o Score crédito de um cliente específico a partir de seu ID único.",
			responses = {
				        @ApiResponse(responseCode = "200", description = "Score do cliente recuperado com sucesso! 🎯",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),

				        @ApiResponse(responseCode = "404", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}		
	)
    @GetMapping("/{id}/calcular")
    public ResponseEntity<ClienteResponseDto> calcularScore(@PathVariable("id") Long id) {
        // Buscar o cliente pelo ID
        ClienteResponseDto clienteResponseDto = clienteService.getClientebyId(id);

        clienteResponseDto.setScore_credito(clienteService.calcularScore(clienteResponseDto));
        
        // Atualizar o cliente (se necessário) e retornar o DTO com o score atualizado
        return ResponseEntity.ok(clienteResponseDto);
    }
        
      
    
    
}
