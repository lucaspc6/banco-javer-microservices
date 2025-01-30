package com.example.clienteStorage.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
	
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
 
public class ClienteCreateDto {
	//@NotBlank(message = "O nome não pode estar vazio.")
	private String nome;
	
	//@NotBlank(message = "O telefone é obrigatório")
	//@Pattern(regexp = "\\d{11}", message = "O telefone deve ter exatamente 11 dígitos numéricos. Exemplo correto: 11912842190")
	private Long telefone;
	
	//@NotNull(message = "Escolha se deseja abrir uma conta corrente ou não.")
	private Boolean correntista;
	
	//@NotNull(message = "O saldo é obrigatório.")
	private Float saldo_cc; //= -1f; 
	
}
		 

