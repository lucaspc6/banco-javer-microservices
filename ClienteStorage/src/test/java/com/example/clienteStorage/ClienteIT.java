package com.example.clienteStorage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.clienteStorage.web.dto.ClienteCreateDto;
import com.example.clienteStorage.web.dto.ClienteResponseDto;
import com.example.clienteStorage.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class ClienteIT {

	@Autowired
	WebTestClient testClient;
	
	@Test
    public void createCliente_ComDadosValidos_RetornaStatus201() {
		
		//criando post
        ClienteResponseDto responseBody = testClient
                .post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 1500f))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();
        
        //validando
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo("Lucas");
        assertThat(responseBody.getTelefone()).isEqualTo(12345678901L);
        assertThat(responseBody.getCorrentista()).isTrue();
        assertThat(responseBody.getSaldo_cc()).isEqualTo(1500f);
	}

    
    @Test
    public void createCliente_ComCamposInvalidos_RetornaStatusDiversos() {
        // 1. Nome é obrigatório e não pode ser vazio
        testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("", 12345678901L, true, 1500f)) // Nome vazio
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .value(error -> assertThat(error.getMessage()).isEqualTo("O nome é obrigatório."));

        // 2. Saldo é obrigatório para criação do cliente
        testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, null)) // Saldo nulo
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .value(error -> assertThat(error.getMessage()).isEqualTo("O saldo é obrigatório"));

        // 3. Telefone deve ter exatamente 11 dígitos
        testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 123456L, true, 1500f)) // Telefone inválido
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .value(error -> assertThat(error.getMessage()).isEqualTo("O telefone do cliente é obrigatório e só pode ter 11 caracteres."));

        // 4. Saldo negativo não é permitido
        testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, -50f)) // Saldo negativo
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .value(error -> assertThat(error.getMessage()).isEqualTo("O saldo deve ser um número positivo."));
        
        // 5. Validando o campo 'Correntista' é obrigatório e deve ser verdadeiro ou falso
        testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, null, 1500f)) // Correntista nulo
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ErrorMessage.class)
                .value(error -> assertThat(error.getMessage()).isEqualTo("Erro inesperado: O correntista é obrigatório e deve ser verdadeiro ou falso."));

        // 6. Número de telefone já cadastrado, escolha outro
        testClient.post()
			    .uri("api/v1/clientes")
			    .contentType(MediaType.APPLICATION_JSON)
			    .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 1500f)) //criando post com telefone 12345678901
			    .exchange()
			    .expectStatus().isCreated()
			    .expectBody(ClienteResponseDto.class)
			    .returnResult().getResponseBody();
        
        testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 0f)) // Duplicando telefone do cliente anterior
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .value(error -> assertThat(error.getMessage()).isEqualTo("Este telefone já foi cadastrado"));
        
        // 7. O nome pode conter apenas letras e espaços.
        testClient.post()
		        .uri("api/v1/clientes")
		        .contentType(MediaType.APPLICATION_JSON)
		        .bodyValue(new ClienteCreateDto("Lu2313", 12345678901L, true, 20f)) // Duplicando telefone do cliente anterior
		        .exchange()
		        .expectStatus().isEqualTo(500)
		        .expectBody(ErrorMessage.class)
		        .value(error -> assertThat(error.getMessage()).isEqualTo("Erro inesperado: O nome deve conter apenas letras e espaços."));
    }
    
    @Test
    public void getClienteById_ComIdValido_RetornaStatus200() {
        // Criando um cliente e capturando o ID
        ClienteResponseDto clienteCriado = testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 1500f)) 
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(clienteCriado, "O cliente não foi criado corretamente.");
        Long clienteId = clienteCriado.getId();
        assertNotNull(clienteId, "O ID do cliente criado é nulo.");

        // Buscando pelo ID e validando o status 200 e os dados retornados
        ClienteResponseDto clienteBuscado = testClient.get()
                .uri("api/v1/clientes/" + clienteId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(clienteBuscado, "A resposta da busca não pode ser nula.");
        assertThat(clienteBuscado.getId()).isEqualTo(clienteId);
        assertThat(clienteBuscado.getNome()).isEqualTo("Lucas");
        assertThat(clienteBuscado.getTelefone()).isEqualTo(12345678901L);
        assertThat(clienteBuscado.getCorrentista()).isTrue();
        assertThat(clienteBuscado.getSaldo_cc()).isEqualTo(1500f);
    }

    
    @Test
    public void getClienteById_ComIdInvalido_RetornaStatus404() {
        testClient
                .get()
                .uri("api/v1/clientes/999")
                .exchange()
                .expectStatus().isNotFound(); //status esperado
    }
    
    @Test
    public void updateCliente_ComDadosValidos_RetornaStatus200() { 
        // Criando um cliente e capturando o ID
        ClienteResponseDto clienteCriado = testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 1500f)) 
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(clienteCriado, "O cliente não foi criado corretamente.");
        Long clienteId = clienteCriado.getId();
        assertNotNull(clienteId, "O ID do cliente criado é nulo.");

        // Atualizando o cliente criado
        ClienteResponseDto clienteAtualizado = testClient.put()
                .uri("api/v1/clientes/" + clienteId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Silva", 11987654322L, false, 2000f)) 
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();

        // Validando os campos do cliente atualizado
        assertNotNull(clienteAtualizado, "A resposta da atualização não pode ser nula.");
        assertThat(clienteAtualizado.getId()).isEqualTo(clienteId);
        assertThat(clienteAtualizado.getNome()).isEqualTo("Silva");
        assertThat(clienteAtualizado.getTelefone()).isEqualTo(11987654322L);
        assertThat(clienteAtualizado.getCorrentista()).isFalse();
        assertThat(clienteAtualizado.getSaldo_cc()).isEqualTo(2000f);
    }

    
    @Test
    public void updateCliente_ComIdInvalido_RetornaStatus404() {
        testClient
                .put()
                .uri("api/v1/clientes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Nunes", 11987654322L, false, 2000f))
                .exchange()
                .expectStatus().isNotFound();
    }
    
    @Test
	public void updateCliente_ComDadosInvalidos_RetornaStatusDiversos() {
	    // Criando um cliente para obter um ID válido
	    Long clienteId = testClient.post()
	            .uri("api/v1/clientes")
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 1500f)) 
	            .exchange()
	            .expectStatus().isCreated()
	            .expectBody(ClienteResponseDto.class)
	            .returnResult()
	            .getResponseBody()
	            .getId();
	
	    // 1. Nome não pode ser vazio (500)
	    testClient.put()
	            .uri("api/v1/clientes/" + clienteId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(new ClienteCreateDto("", 11987654322L, false, 2000f)) 
	            .exchange()
	            .expectStatus().isEqualTo(500)
			    .expectBody(ErrorMessage.class)
		        .value(error -> assertThat(error.getMessage()).isEqualTo("Erro inesperado: O nome não pode estar em branco."));
	
	    // 2. O nome só pode conter letras e espaços (500)
	    testClient.put()
	            .uri("api/v1/clientes/" + clienteId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(new ClienteCreateDto("Lucas123", 11987654322L, false, 2000f)) 
	            .exchange()
	            .expectStatus().isEqualTo(500)
			    .expectBody(ErrorMessage.class)
		        .value(error -> assertThat(error.getMessage()).isEqualTo("Erro inesperado: O nome deve conter apenas letras e espaços."));
	
	    // 3. Saldo negativo não é permitido (400)
	    testClient.put()
	            .uri("api/v1/clientes/" + clienteId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(new ClienteCreateDto("Lucas", 11987654322L, false, -100f)) 
	            .exchange()
	            .expectStatus().isBadRequest()
	            .expectBody(ErrorMessage.class)
		        .value(error -> assertThat(error.getMessage()).isEqualTo("O saldo não pode ser negativo."));
	
	    // 4. Telefone deve ter exatamente 11 dígitos (500)
	    testClient.put()
	            .uri("api/v1/clientes/" + clienteId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(new ClienteCreateDto("Lucas", 11987L, false, 2000f)) 
	            .exchange()
	            .expectStatus().isEqualTo(500)
				.expectBody(ErrorMessage.class)
				.value(error -> assertThat(error.getMessage()).isEqualTo("Erro inesperado: Telefone deve conter 11 dígitos."));
	
	    // 5. Número de telefone já cadastrado (409)
	    Long outroClienteId = testClient.post()
	            .uri("api/v1/clientes")
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(new ClienteCreateDto("Maria", 11987654322L, true, 3000f)) //cadastrando número de telefone
	            .exchange()
	            .expectStatus().isCreated()
	            .expectBody(ClienteResponseDto.class)
	            .returnResult()
	            .getResponseBody()
	            .getId();
	
	    testClient.put()
	            .uri("api/v1/clientes/" + outroClienteId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, false, 2000f)) // Telefone já usado pelo primeiro cliente cadastrado no método.
	            .exchange()
	            .expectStatus().isEqualTo(409)
				.expectBody(ErrorMessage.class)
				.value(error -> assertThat(error.getMessage()).isEqualTo("Este telefone já foi cadastrado"));
	}
    
    @Test
    public void deleteCliente_IdValidoCorrentistaFalse_RetornaStatus204() {
    	//criando cliente com correntista false
    	 Long clienteId = testClient.post()
 	            .uri("api/v1/clientes")
 	            .contentType(MediaType.APPLICATION_JSON)
 	            .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, false, 0f)) 
 	            .exchange()
 	            .expectStatus().isCreated()
 	            .expectBody(ClienteResponseDto.class)
 	            .returnResult()
 	            .getResponseBody()
 	            .getId();
    	 
    	//deletando cliente
        testClient
                .delete()
                .uri("api/v1/clientes/" + clienteId)
                .exchange()
                .expectStatus().isNoContent();
    }
    
    public void deleteCliente_IdValidoCorrentistaTrue_RetornaStatus400() {
    	//criando cliente com correntista true
    	 Long clienteId = testClient.post()
 	            .uri("api/v1/clientes")
 	            .contentType(MediaType.APPLICATION_JSON)
 	            .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 0f)) 
 	            .exchange()
 	            .expectStatus().isCreated()
 	            .expectBody(ClienteResponseDto.class)
 	            .returnResult()
 	            .getResponseBody()
 	            .getId();
    	 
    	//A conta corrente está ativa e não pode ser excluída. Desative-a primeiro.
        testClient
                .delete()
                .uri("api/v1/clientes/" + clienteId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
		        .value(error -> assertThat(error.getMessage()).isEqualTo("A conta corrente está ativa e não pode ser excluída.")); 
    }
    
    @Test
    public void deleteCliente_ComIdInvalido_RetornaStatus500() {
        testClient
                .delete()
                .uri("api/v1/clientes/999")
                .exchange()
                .expectStatus().isNotFound();
    }
    
    @Test
    public void getAllClientes_DeveRetornarListaComStatus200() {
    	//realizando criação de cliente
    	testClient.post()
 	            .uri("api/v1/clientes")
 	            .contentType(MediaType.APPLICATION_JSON)
 	            .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 0f)) 
 	            .exchange()
 	            .expectStatus().isCreated()
 	            .expectBody(ClienteResponseDto.class)
 	            .returnResult()
 	            .getResponseBody();
    	
    	
        List<ClienteResponseDto> responseBody = testClient
                .get()
                .uri("api/v1/clientes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody).isNotEmpty();
    }
    
    @Test
    public void inactivateCC_ComSaldoZerado_RetornaStatus204() {
        // Criando um cliente com saldo zerado
        Long clienteId = testClient.post()
                .uri("api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 0f)) // Saldo zerado
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody()
                .getId();

        // Desativando a conta corrente do cliente
        testClient.patch()
                .uri("api/v1/clientes/" + clienteId + "/desativar")
                .exchange()
                .expectStatus().isNoContent();
        
        // Buscando o cliente novamente para validar se `correntista` agora é `false`
        ClienteResponseDto clienteAtualizado = testClient.get()
                .uri("api/v1/clientes/" + clienteId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult()
                .getResponseBody();
        
        
        assertThat(clienteAtualizado.getCorrentista())
                .isFalse();
    }
    
    @Test
    public void inactivateCC_ClienteNaoEncontrado_RetornaStatus500() {
        testClient
                .patch()
                .uri("api/v1/clientes/999")
                .exchange()
                .expectStatus().isEqualTo(500);
    }
    
    @Test
    public void inactivateCC_SaldoNaoZerado_RetornaStatus400() {
    // Criando um cliente com saldo zerado
    Long clienteId = testClient.post()
            .uri("api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 60f)) // Saldo zerado
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ClienteResponseDto.class)
            .returnResult()
            .getResponseBody()
            .getId();

    // Desativando a conta corrente do cliente
    testClient.patch()
            .uri("api/v1/clientes/" + clienteId + "/desativar")
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorMessage.class)
	        .value(error -> assertThat(error.getMessage()).isEqualTo("É necessário estar com saldo zerado para desativar a conta corrente.")); 
    }
    
    @Test
    public void activateCC_ContaAtivada_RetornaStatus204() {
    // Criando um cliente com a conta corrente desativada
    Long clienteId = testClient.post()
            .uri("api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new ClienteCreateDto("Lucas", 12345678901L, true, 60f)) 
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ClienteResponseDto.class)
            .returnResult()
            .getResponseBody()
            .getId();

    // Ativando a conta corrente do cliente
    testClient.patch()
            .uri("api/v1/clientes/" + clienteId + "/ativar")
            .exchange()
            .expectStatus().isNoContent();
            
         // Buscando o cliente novamente para validar se `correntista` agora é `true`
            ClienteResponseDto clienteAtualizado = testClient.get()
                    .uri("api/v1/clientes/" + clienteId)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(ClienteResponseDto.class)
                    .returnResult()
                    .getResponseBody();
            
            
            assertThat(clienteAtualizado.getCorrentista())
                    .isTrue();
    }
    
    @Test
    public void activateCC_ClienteNaoEncontrado_RetornaStatus500() {
        testClient
        		.patch()
                .uri("api/v1/clientes/999")
                .exchange()
                .expectStatus().isEqualTo(500);
    }
    
    
}
   
	
	
