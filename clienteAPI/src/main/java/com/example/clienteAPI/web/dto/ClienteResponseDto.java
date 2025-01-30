package com.example.clienteAPI.web.dto;

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

public class ClienteResponseDto {
	private Long id;
	private String nome;
	private Long telefone;
	private Boolean correntista;
	private Float score_credito;
	private Float saldo_cc;
}
