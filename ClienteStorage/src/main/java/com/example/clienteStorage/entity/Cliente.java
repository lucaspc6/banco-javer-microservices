package com.example.clienteStorage.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Entity


@Table(name="clientes")
public class Cliente implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@NotBlank(message = "O nome não pode estar vazio.")
	@Column(name = "nome", nullable = false, length = 150)
	private String nome;
	
	//Unicidade de Campos: O campo telefone deve ser único entre os clientes.
	@NotNull(message = "O telefone é obrigatório.") // Não permite nulo
    @Digits(integer = 11, fraction = 0, message = "O telefone deve ter exatamente 11 dígitos numéricos.") // Garante até 11 dígitos inteiros
    @Min(value = 10000000000L, message = "O telefone deve conter exatamente 11 dígitos e não pode começar com 0.") // Valor mínimo válido
    @Max(value = 99999999999L, message = "O telefone deve conter exatamente 11 dígitos.") // Valor máximo válido
	@Column(name = "telefone", nullable = false, unique=true)
	private Long telefone;
	 
	@NotNull(message = "Escolha se deseja abrir uma conta corrente ou não.")
	@Column(name = "correntista", nullable = false)
	private Boolean correntista; //= Boolean.False;
	
	@NotNull(message = "O saldo é obrigatório.")
	@DecimalMin(value = "0.0", inclusive = true, message = "O saldo deve ser maior ou igual a zero.")
	//@PositiveOrZero(message= "O saldo deve não pode ficar negativo.")
	@Column(name = "saldo_cc", nullable = false)
	private Float saldo_cc;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}
	
	//ativando atributo correntista quando o cliente cria a conta
	public void ativarCorrentista() {
		setCorrentista(true);
	}
	
	//desativando correntista quando a pessoa não tem conta ou a inativou
	public void DesativarCorrentista() {
		setCorrentista(false);
	}
	  
}
	 
