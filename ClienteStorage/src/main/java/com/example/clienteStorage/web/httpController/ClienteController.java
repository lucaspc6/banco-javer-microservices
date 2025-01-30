package com.example.clienteStorage.web.httpController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.example.clienteStorage.entity.Cliente;
import com.example.clienteStorage.service.ClienteService;
import com.example.clienteStorage.web.dto.ClienteCreateDto;
import com.example.clienteStorage.web.dto.ClienteResponseDto;
import com.example.clienteStorage.web.dto.mapper.ClienteMapper;
import com.example.clienteStorage.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Clientes", description = "Gerencie os clientes com facilidade: crie, atualize, consulte e remova dados essenciais.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {
	
	@Autowired	
	private ClienteService clienteService;

	@Autowired
	private ClienteMapper clienteMapper;
	
	
	@Operation(summary = "Criação de um novo cliente", description = "Adicione um novo cliente ao sistema com dados essenciais.",
			responses = {
				        @ApiResponse(responseCode = "201", description = "Cliente adicionado com sucesso! 🎉",
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
	public ResponseEntity<ClienteResponseDto> createCliente(@Valid @RequestBody ClienteCreateDto createDto) {
		Cliente cliente = clienteService.salvar(clienteMapper.toCliente(createDto));
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toDto(cliente));	
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
	public ResponseEntity<ClienteResponseDto> updateCliente(
	        @Valid @PathVariable("id") Long id,
	        @RequestBody ClienteCreateDto createDto) {

	    // Converter DTO para entidade Cliente
	    Cliente cliente = clienteMapper.toCliente(createDto);

	    // Atualizar o cliente existente com os novos dados
	    Cliente clienteAtualizado = clienteService.atualizar(id, cliente);

	    // Retornar o DTO de resposta atualizado
	    return ResponseEntity.ok(clienteMapper.toDto(clienteAtualizado));
	}
	
	@Operation(summary = "Listar todos os clientes", description  = "Recupere a lista completa de clientes cadastrados no sistema.",
			responses = {
				        @ApiResponse(responseCode = "200", description = "Clientes listados com sucesso! 📋",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
			}		
	)
	@GetMapping
	public ResponseEntity<List<ClienteResponseDto>> readClientes(){
		List<Cliente> clientes = clienteService.listaCliente();
		return ResponseEntity.ok(clienteMapper.toListDto(clientes)); 	
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
	public ResponseEntity<ClienteResponseDto> getClientebyId(@PathVariable("id") Long id){
		Cliente cliente = clienteService.buscarporId(id);
		return ResponseEntity.ok(clienteMapper.toDto(cliente));
	}
	
	@Operation(summary = "Deletar um cliente", description  = "Exclua um cliente do sistema pelo seu ID único.",
			responses = {
						@ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso! 🗑️"),
					
				        @ApiResponse(responseCode = "404", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
				        
				        @ApiResponse(responseCode = "400", description = "A conta corrente está ativa e não pode ser excluída. Desative-a primeiro.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}		
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCliente(@PathVariable("id") Long id) {
		clienteService.removerPorId(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@Operation(summary = "Desativar a conta corrente do cliente", description  = "Desative a conta corrente do cliente utilizando seu ID. Certifique-se de que o saldo esteja zerado.",
			responses = {
						@ApiResponse(responseCode = "204", description = "Conta corrente do cliente desativada com sucesso. 💳❌"),
					
				        @ApiResponse(responseCode = "500", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
				        
				        @ApiResponse(responseCode = "400", description = "Saldo deve estar zerado para desativar a conta.",
	                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}		
	)
	@PatchMapping("/{id}/desativar")
	public ResponseEntity<Void> inactivateCC(@PathVariable("id") Long id) {
		clienteService.desativarConta(id);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Ativar a conta corrente do cliente", description  = "Ative a conta corrente do cliente novamente utilizando seu ID.",
			responses = {
						@ApiResponse(responseCode = "204", description = "Conta corrente do cliente ativada com sucesso. 💳✔️"),
					
				        @ApiResponse(responseCode = "500", description = "Cliente não encontrado. Verifique o ID e tente novamente.",
				                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
			}		
	)
	@PatchMapping("/{id}/ativar")
	public ResponseEntity<Void> activateCC(@PathVariable("id") Long id) {
		clienteService.ativarConta(id);
		return ResponseEntity.noContent().build();
	}
}